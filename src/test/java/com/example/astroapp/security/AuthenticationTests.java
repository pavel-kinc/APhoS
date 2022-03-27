package com.example.astroapp.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void allowUploadWhenAuthenticated() throws Exception {
        mockMvc
                .perform(get("/upload").with(oauth2Login()))
                .andExpect(status().isOk());
    }

    @Test
    public void redirectToAuthWhenNotAuthenticatedUpload() throws Exception {
        mockMvc
                .perform(get("/upload"))
                .andExpect(redirectedUrlPattern("http://**/oauth2/authorization/google"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void redirectToAuthWhenNotAuthenticatedProfile() throws Exception {
        mockMvc
                .perform(get("/profile"))
                .andExpect(redirectedUrlPattern("http://**/oauth2/authorization/google"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void postRequestForbiddenWithoutCsrf() throws Exception {
        mockMvc.perform(
                        post("/upload/parse").param("pathToDir", "aa")
                                .with(oauth2Login()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void postRequestNotForbiddenWithCsrf() throws Exception {
        // access is not forbidden but excpetion is thrown because of sample dir name
        assertThrows(FileNotFoundException.class, () -> mockMvc.perform(
                post("/upload/parse").param("pathToDir", "aa")
                        .with(oauth2Login())
                        .with(csrf())));
    }
}
