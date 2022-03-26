package com.example.astroapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;


import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UploadControllerTest {

    @Autowired
    MockMvc mockMvc;

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
    public void fileDirectoryNotExisting() {
        assertThrows(FileNotFoundException.class, () -> mockMvc.perform(
                        post("/upload/parse")
                                .param("pathToDir", "hehe")));
    }

    @Test
    public void testingCorrectFile() throws Exception {
        mockMvc.perform(
                post("/upload/parse")
                    .param("pathToDir", "/home/rastislav/Documents/skola/bakalarka/astroApp/src/test/resources/correctFiles")
                        .with(oauth2Login()))
                .andExpect(content().string("0"));
    }
}