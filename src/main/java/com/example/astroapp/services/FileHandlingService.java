package com.example.astroapp.services;

import com.example.astroapp.dao.*;
import com.example.astroapp.dto.PhotoProperties;
import com.example.astroapp.dto.User;
import com.example.astroapp.exceptions.CsvContentException;
import com.example.astroapp.exceptions.CsvRowDataParseException;
import com.example.astroapp.exceptions.IllegalCoordinateFormatException;
import com.example.astroapp.utils.Conversions;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The service for parsing and persisting the uploaded csv file in the database.
 */
@Service
public class FileHandlingService {

    @Autowired
    PhotoPropertiesDao photoPropsDao;

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @Autowired
    FluxDao fluxDao;

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    /**
     * Entry point of the service.
     *
     * @param pathToFile the path to file to parse
     * @throws IOException the io exception
     */
    @Transactional(noRollbackFor = CsvContentException.class)
    public void parseAndPersist(Path pathToFile) throws IOException {
        PhotoProperties photoProperties = new PhotoProperties();
        File file = pathToFile.toFile();
        // first element is the csv schema, second element is the length of the header
        Pair<List<String>, Integer> retPair = parseHeader(file, photoProperties);
        photoPropsDao.savePhotoProps(photoProperties);
        parseCsv(retPair.getFirst(), retPair.getSecond(), file, photoProperties);
    }

    /**
     * Parse csv part of the file (after the initial data).
     *
     * @param schemaRow       the schema of the csv
     * @param startingLine    the starting line of the csv content (length of the initial data to skip)
     * @param file            the file
     * @param photoProperties the photo properties object of the file
     * @throws IOException the io exception
     */
    public void parseCsv(List<String> schemaRow, Integer startingLine,
                          File file, PhotoProperties photoProperties) throws IOException {
        User uploadingUser = userService.getCurrentUser();
        CsvMapper csvMapper = new CsvMapper();
        try (Reader fileReader = new FileReader(file, StandardCharsets.ISO_8859_1)) {
            CsvSchema.Builder csvBuilder = CsvSchema.builder();
            for (String column : schemaRow) {
                csvBuilder.addColumn(column);
            }
            CsvSchema headSchema = csvBuilder
                    .setColumnSeparator(';')
                    .build();
            MappingIterator<Map<String, String>> iterator = csvMapper
                    .readerForMapOf(String.class)
                    .with(headSchema)
                    .readValues(fileReader);
            for (int i = 0; i < startingLine; i++) {
                if (iterator.hasNext()) {
                    iterator.next();
                }
            }
            try {
                while (iterator.hasNextValue()) {
                    Map<String, String> row = iterator.nextValue();
                    if (!row.get("CatalogId").equals("")) {
                        parseAndSaveRow(row, photoProperties, uploadingUser);
                    }
                }
            } catch (ParseException | CsvRowDataParseException e) {
                // one unparsable row shouldn't be a problem
                e.printStackTrace();
            }
        }
    }


    /**
     * Parse the initial data of the file (the data before the csv content) and the
     * schema of the csv content.
     *
     * @param file            the file
     * @param photoProperties empty PhotoProperties object to store the initial data in
     * @return the pair consisting of the schema of the csv part of the file and length of the header
     * @throws IOException the io exception
     */
    public Pair<List<String>, Integer> parseHeader(File file, PhotoProperties photoProperties) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String row;
            int numOfLines = 0;
            boolean exposureBeginFound = false;
            boolean exposureEndFound = false;
            while ((row = br.readLine()) != null) {
                numOfLines++;
                String headerKey = row.split(";")[0];
                if (headerKey.equals("ExposureBegin")) {
                    String jdbcTimestamp = row.split(";")[1].replace("T", " ");
                    photoProperties.setExposureBegin(Timestamp.valueOf(jdbcTimestamp));
                    exposureBeginFound = true;
                }
                if (headerKey.equals("ExposureEnd")) {
                    String jdbcTimestamp = row.split(";")[1].replace("T", " ");
                    photoProperties.setExposureEnd(Timestamp.valueOf(jdbcTimestamp));
                    exposureEndFound = true;
                }
                if (headerKey.equals("Name")) {
                    if (!(exposureBeginFound && exposureEndFound)) {
                        throw new CsvContentException("Exposure time info not found in the header.");
                    }
                    return Pair.of(List.of(row.split(";")), numOfLines);
                }
            }
        }
        throw new CsvContentException("Schema not found.");
    }

    /**
     * Parse row of csv data and persist it in the database.
     *
     * @param row             the row of csv data
     * @param photoProperties the photo properties object of the file
     * @param uploadingUser   the uploading user
     * @throws ParseException the parse exception
     */
    public void parseAndSaveRow(Map<String, String> row, PhotoProperties photoProperties, User uploadingUser)
            throws ParseException {
        Long spaceObjectId = null;
        try {
            if (!(row.get("CatalogId") == null)) {
                 spaceObjectId = saveSpaceObject(row.get("Name"), row.get("Catalog"),
                        row.get("CatalogId"), row.get("CatalogRA"), row.get("CatalogDec"), row.get("CatalogMag"));
            }
            int i = 1;
            String aperture;
            String apertureDev;
            List<Float> apertureDevs = new ArrayList<>();
            List<Float> apertures = new ArrayList<>();
            // getting all columns in form of Ap1..Apn
            // getting all columns in form of Ap1Dev..ApnDev
            while ((aperture = row.get("Ap"+i)) != null) {
                apertures.add(!aperture.equals("saturated") ? Float.parseFloat(aperture) : 0);
                apertureDev = row.get("Ap" + i + "Dev");
                apertureDevs.add(!(apertureDev == null || apertureDev.equals(""))
                        ? Float.parseFloat(apertureDev) : 0);
                i++;
            }
            while ((apertureDev = row.get("Ap" + i + "Dev")) != null) {
                i++;
            }
            saveFlux(row.get("RA"), row.get("Dec"),
                    row.get("ApAuto"), row.get("ApAutoDev"), spaceObjectId, photoProperties,
                    uploadingUser, apertures, apertureDevs);
        } catch (NumberFormatException | IllegalCoordinateFormatException e) {
            throw new CsvRowDataParseException(e);
        }
    }

    /**
     * Save space object to the database.
     *
     * @param name       the name
     * @param catalog    the catalog
     * @param catalogId  the catalog id
     * @param catalogRec the catalog right ascension
     * @param catalogDec the catalog declination
     * @param catalogMag the catalog magnitude
     * @return the id generated by the database
     * @throws ParseException the parse exception
     */
    public Long saveSpaceObject(String name, String catalog, String catalogId,
                                String catalogRec, String catalogDec, String catalogMag)
            throws ParseException {
        float dec = Conversions.angleToFloatForm(catalogDec);
        float rec = Conversions.hourAngleToDegrees(catalogRec);
        float mag = Float.parseFloat(catalogMag);
        String strRec = Conversions.addHourAngleSigns(catalogRec);
        String strDec = Conversions.addAngleSigns(catalogDec);
        try {
            return spaceObjectDao.saveObject(catalogId, name, catalog, strDec, strRec, dec, rec, mag);
        } catch (DataIntegrityViolationException e) {
            return spaceObjectDao.saveObject(catalogId, name, catalog, strDec, strRec, dec, rec, mag);
        }
    }

    /**
     * Save flux to the database.
     *
     * @param strRec           the right ascension string
     * @param strDec           the declination string
     * @param apAuto           the aperture auto
     * @param apAutoDev        the aperture auto deviation
     * @param spaceObjectId    the space object database id
     * @param photoProperties  the photo properties object of the file
     * @param uploadingUser    the uploading user
     * @param aperturesList    the apertures list
     * @param apertureDevsList the aperture deviations list
     * @throws ParseException the parse exception
     */
    public void saveFlux(String strRec, String strDec, String apAuto,
                         String apAutoDev, Long spaceObjectId, PhotoProperties photoProperties,
                         User uploadingUser, List<Float> aperturesList, List<Float> apertureDevsList)
            throws ParseException {
        if (apAuto == null) {
            throw new CsvRowDataParseException("Missing apAuto value");
        }
        float dec = Conversions.angleToFloatForm(strDec);
        float rec = Conversions.hourAngleToDegrees(strRec);
        Float[] apertures = aperturesList.toArray(new Float[0]);
        Float[] apertureDevs = apertureDevsList.toArray(new Float[0]);
        Float apertureAuto = (!apAuto.equals("saturated") ? Float.parseFloat(apAuto) : 0);
        Float apertureAutoDev = (!(apAutoDev == null || apAutoDev.equals(""))
                ? Float.parseFloat(apAutoDev) : 0);
        strRec = Conversions.addHourAngleSigns(strRec);
        strDec = Conversions.addAngleSigns(strDec);
        fluxDao.saveFlux(strRec, strDec, rec, dec, apertureAuto, apertureAutoDev, spaceObjectId,
                uploadingUser.getGoogleSub(), photoProperties.getId(), apertures, apertureDevs);
    }
}


