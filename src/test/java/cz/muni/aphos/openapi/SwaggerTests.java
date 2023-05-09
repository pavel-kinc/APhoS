package cz.muni.aphos.openapi;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.hamcrest.Matchers.containsString;
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
        mockMvc.perform(get("/openapi"))
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
                        ("<div id=\"swagger-ui\"></div>")));
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
