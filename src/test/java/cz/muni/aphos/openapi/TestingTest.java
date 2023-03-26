package cz.muni.aphos.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpGet;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpEntity;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.ParseException;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.io.entity.EntityUtils;
import cz.muni.aphos.AphosApplication;
import cz.muni.aphos.dao.SpaceObjectDao;
import cz.muni.aphos.dao.SpaceObjectDaoImpl;
import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.openapi.models.Catalog;
import cz.muni.aphos.openapi.models.SpaceObjectWithFluxes;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.http.HttpResponse;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@SpringBootTest
public class TestingTest {

    public final String PATH = "https://ip-147-251-21-104.flt.cloud.muni.cz/";
    @Test
    public void testingTest() throws Exception {
        //SpaceObjectWithFluxes star = (SpaceObjectWithFluxes) mockDao.getSpaceObjectByObjectIdCat("779-040824", "UCAC4", true);
        //mockMvc.perform(get("/api/user/Pavel").contentType(MediaType.APPLICATION_JSON));
        //given(mockDao.getSpaceObjectFluxCount(anyLong())).willReturn(null);
        //mockMvc.perform(get("/api/spaceObject/1651651").contentType(MediaType.APPLICATION_JSON))
        //        .andExpect(status().isOk());


        //ResponseEntity<SpaceObjectWithFluxes> star = controller.getSpaceObjectById("807-030174", new Catalog("UCAC4"));
        //System.out.println(star);
        //HttpUriRequest request = new HttpGet( PATH + "api/user/Pavel" );

        // When
        //CloseableHttpResponse response = HttpClientBuilder.create().build().execute( request );
        //System.out.println(response);
        //HttpEntity entity = response.getEntity();
        //String s = EntityUtils.toString(entity, "UTF-8");
        //System.out.println(s);
        assertEquals(1, 1);
    }
}
