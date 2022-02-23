package com.example.astroapp;

import com.example.astroapp.controllers.UploadController;
import com.example.astroapp.services.CustomOAuth2UserService;
import com.example.astroapp.services.FileHandlingService;
import com.example.astroapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UploadController.class)
class UploadControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomOAuth2UserService oAuth2UserService;

    @MockBean
    private FileHandlingService fileHandlingService;

    @Test
    public void uploadingEmptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile
                ("file", "emptyFile.txt",
                        MediaType.TEXT_PLAIN_VALUE, "".getBytes(StandardCharsets.UTF_8));
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        mockMvc.perform(
                        multipart("/upload/save")
                                .file(emptyFile)
                                .param("dirName", "create_new"))
                .andExpect(status().isOk());
    }

    @Test
    public void fileDirectoryNotExisting() {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        assertThrows(FileNotFoundException.class, () -> mockMvc.perform(
                        post("/upload/parse")
                                .param("pathToDir", "hehe")));
    }

    @Test
    public void testingCorrectFile() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        mockMvc.perform(
                post("/upload/parse")
                    .param("pathToDir", "/home/rastislav/Documents/skola/bakalarka/astroApp/src/test/resources/correctFiles"))
                .andExpect(content().string("0"));
    }
}