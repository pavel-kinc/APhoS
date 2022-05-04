package cz.muni.var4astro.controller;

import cz.muni.var4astro.dao.FluxDaoImpl;
import cz.muni.var4astro.dao.PhotoPropertiesDaoImpl;
import cz.muni.var4astro.dao.SpaceObjectDaoImpl;
import cz.muni.var4astro.dto.User;
import cz.muni.var4astro.services.UserService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql({"/sql/schema.sql", "/sql_test_data/test-data-user-only.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
class UploadControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SpaceObjectDaoImpl spaceObjectDao;

    @Autowired
    FluxDaoImpl fluxDao;

    @Autowired
    PhotoPropertiesDaoImpl photoPropertiesDao;

    @MockBean
    UserService userService;

    @Test
    public void uploadingEmptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile
                ("file", "emptyFile.txt",
                        MediaType.TEXT_PLAIN_VALUE, "".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(
                        multipart("/upload/save")
                                .file(emptyFile)
                                .param("dir-name", "create_new"))
                .andExpect(status().isOk());
    }

    @Test
    public void directoryWithFilesNotExisting() throws Exception {
        mockMvc.perform(get("/upload/parse")
                .param("path-to-dir", "notexisting")
                .param("file-count", "1"));
    }

    @Test
    public void correctFileParsingTestShouldRetturnZeroIncorrectCount()
            throws Exception {
        User user = new User("1");
        user.setUsername("name");
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        mockMvc.perform(
                        get("/upload/parse")
                                .param("path-to-dir", "src/test/resources/correct_files")
                                .param("file-count", "1"))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andExpect(request().asyncResult(nullValue()))
                .andExpect(content().string(containsString(
                        "event:COMPLETED\n" +
                        "data:0\n")));
    }

    @Test
    public void incorrectFileParsingTestShouldReturnTwoIncorrectCount()
            throws Exception {
        User user = new User("1");
        user.setUsername("name");
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        mockMvc.perform(
                        get("/upload/parse")
                                .param("path-to-dir", "src/test/resources/incorrect_files")
                                .param("file-count", "2"))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andExpect(request().asyncResult(nullValue()))
                .andExpect(content().string(containsString(
                        "event:COMPLETED\n" +
                                "data:2\n")));
    }
}