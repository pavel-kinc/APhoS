package cz.muni.aphos.openapi;

import cz.muni.aphos.dao.SpaceObjectDao;
import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.openapi.models.Catalog;
import cz.muni.aphos.openapi.models.ComparisonObject;
import cz.muni.aphos.openapi.models.Coordinates;
import cz.muni.aphos.openapi.models.SpaceObjectWithFluxes;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql({"/sql/schema.sql", "/sql_test_data/test-data-all-small-sample.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class SpaceObjectApiTests {

    @Autowired
    private SpaceObjectApiController controller;

    @Autowired
    private SpaceObjectDao spaceDao;

    @Test
    public void spaceObjectByIdsTest(){
        ResponseEntity<SpaceObjectWithFluxes> entity =
                controller.getSpaceObjectById("807-030174", new Catalog("UCAC4"));
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        SpaceObjectWithFluxes star = entity.getBody();
        assertEquals("Name", Objects.requireNonNull(star).getName());
        assertEquals("21h41m55.141s", star.getFluxes().get(0).getRightAsc());
    }

    @Test
    public void catalogNull_spaceObjectByIdsTest(){
        ResponseEntity<SpaceObjectWithFluxes> entity =
                controller.getSpaceObjectById("807-030174", null);
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        SpaceObjectWithFluxes star = entity.getBody();
        assertEquals("Name", Objects.requireNonNull(star).getName());
        assertEquals("21h41m55.141s", star.getFluxes().get(0).getRightAsc());
    }

    @Test
    public void notFound_spaceObjectByIdsTest(){
        // global exception handling is not in tests
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,() -> controller.getSpaceObjectById("123456789", null));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void spaceObjectByParamsTest(){
        ResponseEntity<List<ObjectFluxCount>> entity =
                controller.findSpaceObjectsByParams(null, null, null, null, (float)0, (float)15);
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        List<ObjectFluxCount> stars = entity.getBody();
        assert stars != null;
        assertTrue(stars.size() > 5);
    }

    @Test
    public void multipleParams_spaceObjectByParamsTest(){
        ResponseEntity<List<ObjectFluxCount>> entity =
                controller.findSpaceObjectsByParams(null, new Catalog("UCAC4"), null, Coordinates.example, (float)5, (float)10);
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        List<ObjectFluxCount> stars = entity.getBody();
        assert stars != null;
        assertEquals(1, stars.size());
        assertEquals(Float.valueOf((float)6.26),stars.get(0).getCatalogMag());
    }

    @Test
    public void emptyResult_spaceObjectByParamsTest(){
        ResponseEntity<List<ObjectFluxCount>> entity =
                controller.findSpaceObjectsByParams(null, new Catalog("UCAC4"), "RandomName", null, (float)5, (float)10);
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        List<ObjectFluxCount> stars = entity.getBody();
        assertTrue(stars.isEmpty());
        assertEquals(2, 1);
    }

    @Test
    public void badCoordinates_spaceObjectByParamsTest(){

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,() ->
                        controller.findSpaceObjectsByParams(null, new Catalog("UCAC4"), null,
                                "RandomString", (float)5, (float)10));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void getComparisonByIdentificatorsTest(){
        ResponseEntity<ComparisonObject> entity =
                controller.getComparisonByIdentificators("779-040824",null, "801-032283", null);
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        ComparisonObject comparison = entity.getBody();
        assert comparison != null;
        assertEquals(2, comparison.data().size());
        assertEquals(2,comparison.variable().getNumberOfFluxes());
        assertEquals(1,comparison.comparison().getNumberOfFluxes());
        assertEquals("06-11-2021",comparison.data().get(0).getNight().getFirstDateOfTheNight());
        assertEquals("177866.53",comparison.data().get(0).getApAuto());
        assertEquals((float) -0.062,comparison.data().get(0).getMagnitude(), 0.001);
        assertEquals("auto",comparison.data().get(0).getNight().getApToBeUsed());
    }

    @Test
    public void uploadFileTest() throws IOException {
        //807-030174
        File myFile = new File("src/test/resources/correct_files/correct_file_missing_scheme.csv");
        MultipartFile multipartFile = new MockMultipartFile("test.csv", new FileInputStream(myFile));
        Long count = spaceDao.getSpaceObjectFluxCount(8961L);
        controller.uploadCSV(multipartFile);
        assertEquals(count + 1, (long) spaceDao.getSpaceObjectFluxCount(8961L));
    }

    @Test
    public void wrongFile_uploadFileTest() throws IOException {
        //807-030174
        File myFile = new File("src/test/resources/incorrect_files/scheme_missing.csv");
        MultipartFile multipartFile = new MockMultipartFile("test.csv", new FileInputStream(myFile));
        Long count = spaceDao.getSpaceObjectFluxCount(8961L);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->controller.uploadCSV(multipartFile));
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());
        assertEquals((long)count, (long) spaceDao.getSpaceObjectFluxCount(8961L));
    }
}
