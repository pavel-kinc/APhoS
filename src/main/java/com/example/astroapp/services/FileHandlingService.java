package com.example.astroapp.services;

import com.example.astroapp.dao.*;
import com.example.astroapp.entities.PhotoProperties;
import com.example.astroapp.entities.User;
import com.example.astroapp.exceptions.CsvContentException;
import com.example.astroapp.utils.Conversions;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class FileHandlingService {

    @Autowired
    PhotoPropsRepo propsRepo;

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @Autowired
    FluxDao fluxDao;

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @Transactional
    public void store(Path pathToFile) throws IOException {
        PhotoProperties photoProperties = new PhotoProperties();
        File file = pathToFile.toFile();
        // second element of pair is length of the header
        Pair<List<String>, Integer> retPair = parseHeader(file, photoProperties);
        propsRepo.save(photoProperties);
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
            while (iterator.hasNextValue()) {
                Map<String, String> row = iterator.nextValue();
                if (!row.get("CatalogId").equals("")) {
                    saveRow(row, photoProperties, uploadingUser);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private Pair<List<String>, Integer> parseHeader(File file, PhotoProperties photoProperties) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String row;
            int numOfLines = 0;
            while ((row = br.readLine()) != null) {
                numOfLines++;
                String headerKey = row.split(";")[0];
                if (headerKey.equals("ExposureBegin")) {
                    String jdbcTimestamp = row.split(";")[1].replace("T", " ");
                    photoProperties.setExposureBegin(Timestamp.valueOf(jdbcTimestamp));
                }
                if (headerKey.equals("ExposureEnd")) {
                    String jdbcTimestamp = row.split(";")[1].replace("T", " ");
                    photoProperties.setExposureEnd(Timestamp.valueOf(jdbcTimestamp));
                }
                if (headerKey.equals("Name")) {
                    return Pair.of(List.of(row.split(";")), numOfLines);
                }
            }
        }
        throw new CsvContentException("Schema not found.");
    }

    public void saveRow(Map<String, String> row, PhotoProperties photoProperties, User uploadingUser)
            throws ParseException {
        Long spaceObjectId = saveObject(row.get("Name"), row.get("Catalog"),
                row.get("CatalogId"), row.get("CatalogRA"), row.get("CatalogDec"), row.get("CatalogMag"));
        int i = 1;
        String aperture;
        List<Float> apertures = new ArrayList<>();
        // getting all columns in form of Ap1..Apn
        while ((aperture = row.get("Ap"+i)) != null) {
            apertures.add(!aperture.equals("saturated") ? Float.parseFloat(aperture) : 0);
            i++;
        }
        saveFlux(row.get("RA"), row.get("Dec"),
                row.get("ApAuto"), spaceObjectId, photoProperties, uploadingUser, apertures);
    }

    public Long saveObject(String name, String catalog, String catalogId,
                                  String catalogRec, String catalogDec, String catalogMag)
            throws ParseException {
        float dec = Conversions.angleToFloatForm(catalogDec);
        float rec = Conversions.hourAngleToDegrees(catalogRec);
        float mag = Float.parseFloat(catalogMag);
        String strRec = Conversions.addAngleSigns(catalogRec);
        String strDec = Conversions.addAngleSigns(catalogDec);
        try {
            return spaceObjectDao.saveObject(catalogId, name, catalog, strDec, strRec, dec, rec, mag);
        } catch (Exception e) {
            return spaceObjectDao.saveObject(catalogId, name, catalog, strDec, strRec, dec, rec, mag);
        }
    }

    public void saveFlux(String strRec, String strDec, String apAuto,
                         Long spaceObjectId, PhotoProperties photoProperties, User uploadingUser, List<Float> aperturesList)
            throws ParseException {
        float dec = Conversions.angleToFloatForm(strDec);
        float rec = Conversions.hourAngleToDegrees(strRec);
        Float[] apertures = aperturesList.toArray(new Float[0]);
        Float apertureAuto = (!apAuto.equals("saturated") ? Float.parseFloat(apAuto) : 0);
        strRec = Conversions.addHourAngleSigns(strRec);
        strDec = Conversions.addAngleSigns(strDec);
        fluxDao.saveFlux(strRec, strDec, rec, dec, apertureAuto, spaceObjectId,
                uploadingUser.getGoogleSub(), photoProperties.getId(), apertures);
    }
}


