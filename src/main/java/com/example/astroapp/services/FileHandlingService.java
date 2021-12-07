package com.example.astroapp.services;

import com.example.astroapp.dao.*;
import com.example.astroapp.entities.PhotoProperties;
import com.example.astroapp.exceptions.CsvContentException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
public class FileHandlingService {

    @Autowired
    PhotoPropsRepo propsRepo;

    @Autowired
    ObjectRepo objectRepo;

    @Autowired
    RowDataParserService rowDataParser;

    public void store(MultipartFile multipartFile) throws IOException {
        PhotoProperties photoProperties = new PhotoProperties();
        File file = multipartFileToNormal(multipartFile);
        Pair<List<String>, Integer> retPair = parseHeader(file, photoProperties);
        propsRepo.save(photoProperties);
        parseCsv(retPair.getFirst(), retPair.getSecond(), file, photoProperties);
    }

    private void parseCsv(List<String> schemaRow, Integer startingLine,
                          File file, PhotoProperties photoProperties) throws IOException {
        rowDataParser.setPhotoProperties(photoProperties);
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
                rowDataParser.saveRow(row);
            }
            objectRepo.updateCoordinates();
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

}
