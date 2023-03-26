package cz.muni.aphos.openapi;

import cz.muni.aphos.dao.SpaceObjectDao;
import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.openapi.models.Catalog;
import cz.muni.aphos.openapi.models.ComparisonObject;
import cz.muni.aphos.openapi.models.Coordinates;
import cz.muni.aphos.openapi.models.SpaceObjectWithFluxes;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
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
import java.util.List;
import java.util.Objects;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void findByParams_noParamsTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/spaceObject/findByParams")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andReturn();
        System.out.println(result);
    }
}
