package cz.muni.aphos.security;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase(provider = ZONKY)
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

    //@Test
    //public void postRequestForbiddenWithoutCsrf() throws Exception {
    //    mockMvc.perform(
    //                    post("/upload/save")
    //                            .param("file", "aa")
    //                            .param("dir-name", "create_new")
    //                            .with(oauth2Login()))
    //            .andExpect(status().isForbidden());
    //}

    @Test
    public void postRequestNotForbiddenWithCsrf() {
        MockMultipartFile emptyFile = new MockMultipartFile
                ("file", "emptyFile.txt",
                        MediaType.TEXT_PLAIN_VALUE, "".getBytes(StandardCharsets.UTF_8));
        // access is not forbidden so no exception is thrown
        assertDoesNotThrow(() -> mockMvc.perform(
                multipart("/upload/save").file(emptyFile)
                        .requestAttr("dir-name", "create_new")
                        .with(oauth2Login())
                        .with(csrf())));
    }
}
