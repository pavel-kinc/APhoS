package cz.muni.aphos.openapi;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpGet;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpEntity;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.ParseException;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.io.entity.EntityUtils;
import cz.muni.aphos.AphosApplication;
import cz.muni.aphos.dao.SpaceObjectDao;
import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.openapi.models.Catalog;
import cz.muni.aphos.openapi.models.SpaceObjectWithFluxes;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import net.minidev.json.JSONObject;
import org.junit.Test;
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
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.http.HttpResponse;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@Sql({"/sql/schema.sql", "/sql_test_data/test-data-all-small-sample.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class OpenApiTests {

    @Autowired
    SpaceObjectDao controller;

    @Autowired
    SpaceObjectApiController contr;

    public static final String PATH = "https://ip-147-251-21-104.flt.cloud.muni.cz/";

    @Test
    public void testingTest() throws IOException, ParseException {
        SpaceObjectWithFluxes star = (SpaceObjectWithFluxes) controller.getSpaceObjectByObjectIdCat("779-040824", "UCAC4", true);
        System.out.println(star);
        System.out.println(star.getFluxes());
        ResponseEntity<SpaceObjectWithFluxes> star1 = contr.getSpaceObjectById("807-030174", new Catalog("UCAC4"));
        System.out.println(star1.getBody().getName());


        //ResponseEntity<SpaceObjectWithFluxes> star = controller.getSpaceObjectById("807-030174", new Catalog("UCAC4"));
        //System.out.println(star);
        //HttpUriRequest request = new HttpGet( PATH + "api/user/Pavel" );

        // When
        //CloseableHttpResponse response = HttpClientBuilder.create().build().execute( request );
        //System.out.println(response);
        //HttpEntity entity = response.getEntity();
        //String s = EntityUtils.toString(entity, "UTF-8");
        //System.out.println(s);
        //assertEquals(1, 1);
    }
}
