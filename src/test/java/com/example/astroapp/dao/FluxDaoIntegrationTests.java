package com.example.astroapp.dao;

import com.example.astroapp.dto.FluxUserTime;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.List;

import static com.example.astroapp.utils.Conversions.angleToFloatForm;
import static com.example.astroapp.utils.Conversions.hourAngleToDegrees;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"/schema.sql", "/sql_test_data/test-data-objects-with-fluxes.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class FluxDaoIntegrationTests {

    @Autowired
    FluxDao fluxDao;

    @Test
    public void returnTwoFluxeUserTimesForObject() {
        assertEquals(2,
                fluxDao.getFluxesByObjId(9605L, 9087L).size());
    }

    @Test
    public void sameNightOnTwoFluxes() {
        List<FluxUserTime> fluxes =  fluxDao.getFluxesByObjId(10482L, 10406L);
        assertEquals(fluxes.get(0).getNight(), fluxes.get(1).getNight());
    }

    @Test
    public void savedFluxIsReturned() throws ParseException {
        Long id = fluxDao.saveFlux("21h24m53.861s", "+69°47'42.94\"", hourAngleToDegrees("21 24 53.861"),
                angleToFloatForm("69 47 42.94"), 29474.0507F, 10059L,
                "108133573258632378409", 295L, new Float[10]);
        assertEquals(fluxDao.fluxExists(id), id.longValue());

    }
}
