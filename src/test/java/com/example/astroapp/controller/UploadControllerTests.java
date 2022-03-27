package com.example.astroapp.controller;

import com.example.astroapp.dao.FluxDao;
import com.example.astroapp.dao.PhotoPropertiesDao;
import com.example.astroapp.dao.SpaceObjectDao;
import com.example.astroapp.entities.User;
import com.example.astroapp.services.UserService;
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

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql({"/schema.sql", "/sql_test_data/test-data-user-only.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
class UploadControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @Autowired
    FluxDao fluxDao;

    @Autowired
    PhotoPropertiesDao photoPropertiesDao;

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
                                .param("dirName", "create_new"))
                .andExpect(status().isOk());
    }

    @Test
    public void directoryWithFilesNotExisting() {
        assertThrows(FileNotFoundException.class, () -> mockMvc.perform(
                        post("/upload/parse")
                                .param("pathToDir", "hehe")));
    }

    @Test
    public void correctFileParsingTestShouldRetturnZeroIncorrectCount()
            throws Exception {
        User user = new User("1");
        user.setUsername("name");
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        mockMvc.perform(
                post("/upload/parse")
                    .param("pathToDir", "src/test/resources/correctFiles"))
                .andExpect(content().string("0"));
    }

    @Test
    public void incorrectFileParsingTestShouldReturnOneIncorrectCount()
            throws Exception {
        User user = new User("1");
        user.setUsername("name");
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        mockMvc.perform(
                        post("/upload/parse")
                                .param("pathToDir", "src/test/resources/incorrectFiles"))
                .andExpect(content().string("1"));
    }
}