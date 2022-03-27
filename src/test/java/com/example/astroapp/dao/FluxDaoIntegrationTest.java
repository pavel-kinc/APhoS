package com.example.astroapp.dao;

import com.example.astroapp.dto.FluxUserTime;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"/schema.sql", "/sql_test_data/test-data-objects-with-fluxes.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class FluxDaoIntegrationTest {

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
}
