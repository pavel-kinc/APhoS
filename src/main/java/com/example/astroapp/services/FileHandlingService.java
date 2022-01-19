package com.example.astroapp.services;

import com.example.astroapp.dao.*;
import com.example.astroapp.entities.*;
import com.example.astroapp.exceptions.CsvContentException;
import com.example.astroapp.utils.UnitConversions;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static org.springframework.transaction.annotation.Isolation.*;

@Service
public class FileHandlingService {

    @Autowired
    PhotoPropsRepo propsRepo;

    @Autowired
    ObjectRepo objectRepo;

    @Autowired
    FluxRepo fluxRepo;

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

    private void parseCsv(List<String> schemaRow, Integer startingLine,
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
            //objectRepo.updateCoordinates();
            fluxRepo.updateCoordinates();
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

    public void saveRow(Map<String, String> row, PhotoProperties photoProperties, User uploadingUser) throws ParseException {
        SpaceObject spaceObject = saveObject(row.get("Name"), row.get("Catalog"),
                row.get("CatalogId"), row.get("CatalogRA"), row.get("CatalogDec"), row.get("CatalogMag"));
        Flux flux = saveFlux(row.get("RA"), row.get("Dec"),
                row.get("ApAuto"), spaceObject, photoProperties, uploadingUser);
//        int i = 1;
//        String aperture;
//        List<Aperture> apertures = new ArrayList<>();
//        // getting all columns in form of Ap1..Apn
//        while ((aperture = row.get("Ap"+i)) != null) {
//            apertures.add(new Aperture(flux, aperture));
//            i++;
//        }
//        apertureRepo.saveAll(apertures);
    }

    public SpaceObject saveObject(String name, String catalog, String catalogID,
                                  String catalogRec, String catalogDec, String catalogMag) throws ParseException {
        SpaceObject spaceObject = new SpaceObject();
        spaceObject.setName(name);
        spaceObject.setCatalog(catalog);
        spaceObject.setCatalogID(catalogID);
        float dec = UnitConversions.angleToFloatForm(catalogDec);
        spaceObject.setCatalogDec(dec);
        float rec = UnitConversions.hourAngleToDegrees(catalogRec);
        spaceObject.setCatalogRec(rec);
        spaceObject.setCatalogMag(Float.parseFloat(catalogMag));
        return objectService.checkIfExistsAndSave(spaceObject);
    }

    public Flux saveFlux(String strRec, String strDec, String apAuto,
                         SpaceObject object, PhotoProperties photoProperties, User uploadingUser) throws ParseException {
        Flux flux = new Flux();
        float dec = UnitConversions.angleToFloatForm(strDec);
        flux.setDec(dec);
        float rec = UnitConversions.hourAngleToDegrees(strRec);
        flux.setRec(rec);
        flux.setApertureAuto(!apAuto.equals("saturated") ? Float.parseFloat(apAuto) : 0);
        flux.setObject(object);
        flux.setPhotoProperty(photoProperties);
        flux.setUser(uploadingUser);
        fluxRepo.save(flux);
        return flux;
    }

    public void saveAperture(String apertureStr, Flux flux) {
        Aperture aperture = new Aperture();
        aperture.setValue(!apertureStr.equals("saturated") ? Float.parseFloat(apertureStr) : 0);
        aperture.setFlux(flux);
        apertureRepo.save(aperture);
    }
}


