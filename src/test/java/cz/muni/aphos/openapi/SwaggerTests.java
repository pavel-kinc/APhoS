package cz.muni.aphos.openapi;

import cz.muni.aphos.config.SwaggerConfig;
import cz.muni.aphos.dao.SpaceObjectDao;
import cz.muni.aphos.dto.User;
import cz.muni.aphos.openapi.models.Catalog;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest()
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(provider = ZONKY)
public class SwaggerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void openapiDocsAccessibleTest() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString
                        ("This is Amateur Photometric Survey (APhoS) Application Programming Interface.")));
    }

    @Test
    public void swaggerAccessibleTest() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString
                        ("<!DOCTYPE html>")))
                .andExpect(content().string(containsString
                ("<div id=\"swagger-ui\"></div>")));;
    }

    @Test
    public void swaggerRedirectTest() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/swagger-ui/index.html"));
    }

    @Test
    public void swaggerEndpointTest() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html#/SpaceObject/getSpaceObjectById"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString
                ("<!DOCTYPE html>")));
    }

}
