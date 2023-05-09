package cz.muni.aphos.controller;

import cz.muni.aphos.dao.SpaceObjectDaoImpl;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/sql/schema.sql", "/sql_test_data/test-data-objects-with-fluxes.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class HomeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SpaceObjectDaoImpl spaceObjectDao;

    @Test
    public void homeControllerReturnsHtml() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Search for an object:")));
    }

    @Test
    public void homeControllerSearchingObjectReturnsHtml() throws Exception {
        mockMvc.perform(get("/search?right-ascension=&dec=" +
                        "&radius=&name=&min-mag=0&max-mag=15&catalog=UCAC4&object-id="))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Choose an object:")));
    }

}
