package com.example.astroapp.services;

import com.example.astroapp.dao.*;
import com.example.astroapp.entities.PhotoProperties;
import com.example.astroapp.entities.User;
import com.example.astroapp.exceptions.CsvContentException;
import com.example.astroapp.utils.UnitConversions;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    ApertureRepo apertureRepo;

    @Autowired
    UserRepo userRepo;

    public void store(MultipartFile multipartFile) throws IOException {
        PhotoProperties photoProperties = new PhotoProperties();
        File file = multipartFileToNormal(multipartFile);
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

    private File multipartFileToNormal(MultipartFile file) throws IOException {
        File normalFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(normalFile);
        return normalFile;
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
        Long fluxId = saveFlux(row.get("RA"), row.get("Dec"),
                row.get("ApAuto"), spaceObjectId, photoProperties, uploadingUser, apertures);
    }

    public Long saveObject(String name, String catalog, String catalogId,
                                  String catalogRec, String catalogDec, String catalogMag)
            throws ParseException {
        float dec = UnitConversions.angleToFloatForm(catalogDec);
        float rec = UnitConversions.hourAngleToDegrees(catalogRec);
        float mag = Float.parseFloat(catalogMag);
        return spaceObjectDao.saveObject(catalogId, name, catalog, dec, rec, mag);
    }

    public Long saveFlux(String strRec, String strDec, String apAuto,
                         Long spaceObjectId, PhotoProperties photoProperties, User uploadingUser, List<Float> aperturesList)
            throws ParseException {
        float dec = UnitConversions.angleToFloatForm(strDec);
        float rec = UnitConversions.hourAngleToDegrees(strRec);
        Float[] apertures = aperturesList.toArray(new Float[0]);
        Float apertureAuto = (!apAuto.equals("saturated") ? Float.parseFloat(apAuto) : 0);
        return fluxDao.saveFlux(rec, dec, apertureAuto, spaceObjectId,uploadingUser.getGoogleSub(),
                photoProperties.getId(), apertures);
    }
}


