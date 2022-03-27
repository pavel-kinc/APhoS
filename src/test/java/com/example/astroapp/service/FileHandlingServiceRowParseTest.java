package com.example.astroapp.service;


import com.example.astroapp.dao.FluxDao;
import com.example.astroapp.dao.PhotoPropertiesDao;
import com.example.astroapp.dao.SpaceObjectDao;
import com.example.astroapp.entities.PhotoProperties;
import com.example.astroapp.entities.User;
import com.example.astroapp.exceptions.CsvRowDataParseException;
import com.example.astroapp.services.FileHandlingService;
import com.example.astroapp.services.UserService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"/schema.sql", "/sql_test_data/test-data-user-and-photoprops.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class FileHandlingServiceRowParseTest {

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @Autowired
    FluxDao fluxDao;

    @Autowired
    PhotoPropertiesDao photoPropertiesDao;

    @Autowired
    FileHandlingService fileHandlingService;

    @MockBean
    UserService userService;

    private User sampleUser = new User("1");

    private PhotoProperties photoProperties = new PhotoProperties(0);

    private Map<String, String> defaultRow = Stream.of(new String[][]{
            {"Name", "GK Cep"},
            {"RA", "21 41 55.318"},
            {"Dec", "+71 18 38.60"},
            {"Catalog", "UCAC4"},
            {"CatalogId", "807-030174"},
            {"CatalogRA", "21 41 55.291"},
            {"CatalogDec", "+71 18 41.12"},
            {"CatalogMag", "6.26"},
            {"ApAuto", "271001.483"},
            {"Ap1", "145813.047"},
            {"Ap2", "167942.751"},
            {"Ap3", "161769.494"},
            {"Ap4", "119508.426"},
            {"Ap5", "184859.687"},
            {"Ap6", "saturated"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    @Test()
    public void correctRowNoExceptionIsThrown() throws ParseException {
        fileHandlingService.saveRow(defaultRow, photoProperties, sampleUser);
    }

    @Test()
    public void correctRowNoAperturesNoExceptionIsThrown() throws ParseException {
        Map<String, String> rowWithoutApertures = new HashMap<>(defaultRow);
        for (int i = 1; i < 7; i++) {
            rowWithoutApertures.remove("Ap"+i);
        }
        fileHandlingService.saveRow(rowWithoutApertures, photoProperties, sampleUser);
    }

    @Test
    public void correctRowMissingNonEssentialAttribute() throws ParseException {
        Map<String, String> rowMissing = new HashMap<>(defaultRow);
        rowMissing.remove("Name");
        fileHandlingService.saveRow(rowMissing, photoProperties, sampleUser);
    }

    @Test
    public void correctRowMissingCatalogAttributes() throws ParseException {
        Map<String, String> rowMissing = new HashMap<>(defaultRow);
        rowMissing.remove("Catalog");
        rowMissing.remove("CatalogId");
        rowMissing.remove("CatalogRA");
        rowMissing.remove("CatalogDec");
        rowMissing.remove("CatalogMag");
        fileHandlingService.saveRow(rowMissing, photoProperties, sampleUser);
    }

    @Test
    public void incorrectRowMissingApAuto() {
        Map<String, String> rowMissing = new HashMap<>(defaultRow);
        rowMissing.remove("ApAuto");
        assertThrows(CsvRowDataParseException.class, () ->
                fileHandlingService.saveRow(rowMissing, photoProperties, sampleUser));
    }

    @Test
    public void magnitudeValueNotNumeric() {
        Map<String, String> rowNotNumericMag = new HashMap<>(defaultRow);
        rowNotNumericMag.put("CatalogMag", "not a number");
        assertThrows(CsvRowDataParseException.class, () ->
                fileHandlingService.saveRow(rowNotNumericMag, photoProperties, sampleUser));
    }

    @Test
    public void rightAscensionWrongFormat() {
        Map<String, String> rowWrongRa = new HashMap<>(defaultRow);
        rowWrongRa.put("RA", "21 a 55.318");
        assertThrows(ParseException.class, () ->
                fileHandlingService.saveRow(rowWrongRa, photoProperties, sampleUser));
    }

    @Test
    public void declinationWrongFormat() {
        Map<String, String> rowWrongDec = new HashMap<>(defaultRow);
        rowWrongDec.put("Dec", "18 38.60");
        assertThrows(CsvRowDataParseException.class, () ->
                fileHandlingService.saveRow(rowWrongDec, photoProperties, sampleUser));
    }
}
