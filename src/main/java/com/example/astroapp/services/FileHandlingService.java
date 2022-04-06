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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Transactional(noRollbackFor = CsvContentException.class)
    public void store(Path pathToFile) throws IOException {
        PhotoProperties photoProperties = new PhotoProperties();
        File file = pathToFile.toFile();
        // first element is the csv schema, second element is the length of the header
        Pair<List<String>, Integer> retPair = parseHeader(file, photoProperties);
        photoPropsDao.savePhotoProps(photoProperties);
        parseCsv(retPair.getFirst(), retPair.getSecond(), file, photoProperties);
    }

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
                        saveRow(row, photoProperties, uploadingUser);
                    }
                }
            } catch (ParseException | CsvRowDataParseException e) {
                // one unparsable row shouldn't be a problem
                e.printStackTrace();
            }
        }
    }


    private Pair<List<String>, Integer> parseHeader(File file, PhotoProperties photoProperties) throws IOException {
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

    public void saveRow(Map<String, String> row, PhotoProperties photoProperties, User uploadingUser)
            throws ParseException {
        Long spaceObjectId = null;
        try {
            if (!(row.get("CatalogId") == null)) {
                 spaceObjectId = saveObject(row.get("Name"), row.get("Catalog"),
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

    public Long saveObject(String name, String catalog, String catalogId,
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


