package cz.muni.aphos.openapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.aphos.dao.SpaceObjectDao;
import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.dto.SpaceObject;
import cz.muni.aphos.openapi.models.Catalog;
import cz.muni.aphos.openapi.models.ComparisonObject;
import cz.muni.aphos.openapi.models.Coordinates;
import cz.muni.aphos.openapi.models.SpaceObjectWithFluxes;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Sql({"/sql/schema.sql", "/sql_test_data/test-data-all-small-sample.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class ApiMockTests {

    @Autowired
    private SpaceObjectApiController controller;

    @Autowired
    private SpaceObjectDao spaceDao;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void findByParamsJSON_noParamsTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/space-objects/search-by-params")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        List<ObjectFluxCount> star = spaceDao.queryObjects("","","",
                "","0", "15","All catalogues","");
        assertTrue(result.getResponse().getContentAsString().contains
                ("<item><id>807-030174</id><catalog>UCAC4</catalog><name>Name</name><rightAsc>21h41m55.291s</rightAsc>"));
    }

    @Test
    public void findByParams_someParamsTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/space-objects/search-by-params")
                        .param("catalog", "UCAC4")
                        .param("coordinates", "{\"rightAsc\":\"21:41:55.291\",\"declination\":\"71:18:41.12\",\"radius\":1}")
                        .param("minMag", "6.1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        List<ObjectFluxCount> stars = spaceDao.queryObjects("21:41:55.291","71:18:41.12","1",
                "","6.1", "15","UCAC4","");

        assertTrue(result.getResponse().getContentAsString().length() > 50);

        ObjectMapper mapper = new ObjectMapper();
        // readTree is a must, otherwise the objects are not equal (with valueToTree)
        JsonNode expected = mapper.readTree(mapper.writeValueAsString(stars));
        JsonNode res = mapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        assertEquals(expected, res);
        // test if the result json contains values of catalog
        List<JsonNode> catalogs = res.findValues("catalog");
        assertEquals("\"UCAC4\"",catalogs.get(0).toString());
    }

    @Test
    public void findByParams_badRequestTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/space-objects/search-by-params")
                        .param("coordinates", "Random String"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andReturn();
    }

    @Test
    public void findByParams_IllegalArgumentTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/space-objects/search-by-params")
                        .param("minMag", "abcd"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andReturn();
    }

    @Test
    public void findUser_nonExistent() throws Exception {
        mockMvc.perform(get("/api/users/NonExistentsas4891"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void noCatalog_getComparison() throws Exception {
        mockMvc.perform(get("/api/space-objects/comparison")
                        .param("originalId","779-040824")
                        .param("referenceId","801-032283"))
                .andExpect(status().isOk())
                // test that json contains object_id (SpaceObject-catalogId) as id
                .andExpect(jsonPath("$.variable.id").value("779-040824"))
                .andExpect(jsonPath("$.data").isArray());
    }
}
