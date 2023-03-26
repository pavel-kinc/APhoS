package cz.muni.aphos.dao;

import cz.muni.aphos.dto.SpaceObject;
import cz.muni.aphos.utils.Conversions;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static java.lang.Math.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql({"/sql/schema.sql", "/sql_test_data/test-data-object-only.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class SpaceObjectDaoIntegrationTests {

    @Autowired
    SpaceObjectDaoImpl spaceObjectDao;

    @Test
    public void returnCorrectObjectById() {
        SpaceObject expectedObject = new SpaceObject("801-033364", "", "UCAC4",
                "+70°07'57.26\"", "22h10m38.791s", 6.81F);
        AssertionsForClassTypes.assertThat(spaceObjectDao.queryObjects("", "", "", "",
                "0", "15", "UCAC4", "801-033364").get(0))
                .usingRecursiveComparison()
                .ignoringFields("id", "fluxes", "numberOfFluxes")
                .isEqualTo(expectedObject);
    }

    @Test
    public void returnCorrectObjectByIdAndMagnitude() {
        SpaceObject expectedObject = new SpaceObject("801-033364", "", "UCAC4",
                "+70°07'57.26\"", "22h10m38.791s", 6.81F);
        AssertionsForClassTypes.assertThat(spaceObjectDao.queryObjects("", "", "", "",
                "6.8", "6.9", "UCAC4", "801-033364").get(0))
                .usingRecursiveComparison()
                .ignoringFields("id", "fluxes", "numberOfFluxes")
                .isEqualTo(expectedObject);
    }

    @Test
    public void correctObjectByIdButOutOfMagnitudeInterval() {
        assertEquals(0, spaceObjectDao.queryObjects("", "", "", "",
                "6.9", "7", "UCAC4", "801-033364").size());
    }

    @Test
    public void correctNumberOfObjectsInMagnitudeInterval() {
        assertEquals(19, spaceObjectDao.queryObjects("", "", "", "",
                "6", "8", "UCAC4", "").size());
    }

    @Test
    public void returnCorrectObjectByName() {
        SpaceObject expectedObject = new SpaceObject("805-031770", "GK Cep", "UCAC4",
                "+70°49'23.58\"", "21h30m59.150s", 8.42F);
        AssertionsForClassTypes.assertThat(spaceObjectDao.queryObjects("", "", "", "GK Cep",
                "0", "15", "UCAC4", "").get(0))
                .usingRecursiveComparison()
                .ignoringFields("id", "fluxes", "numberOfFluxes")
                .isEqualTo(expectedObject);
    }

    @Test
    public void returnCorrectObjectByCoordinatesAndSmallRadius() {
        SpaceObject expectedObject = new SpaceObject("805-031770", "GK Cep", "UCAC4",
                "+70°49'23.58\"", "21h30m59.150s", 8.42F);
        AssertionsForClassTypes.assertThat(spaceObjectDao.queryObjects("21:30:59.150", "70:49:23.58", "0.0001", "GK Cep",
                "0", "15", "UCAC4", "").get(0))
                .usingRecursiveComparison()
                .ignoringFields("id", "fluxes", "numberOfFluxes")
                .isEqualTo(expectedObject);
    }

    @Test
    public void returnCorrectNumberOfObjectsByCoordinatesAndSmallRadius() {
        assertEquals(1, spaceObjectDao.queryObjects("21:30:59.150",
                "70:49:23.58", "0.0001", "GK Cep",
                "0", "15", "UCAC4", "").size());
    }

    @Test
    public void returnCorrectNumberOfObjectsByCoordinatesAndMediumRadius() {
        assertEquals(3, spaceObjectDao.queryObjects("21:30:59.150",
                "70:49:23.58", "0.1", "",
                "0", "15", "UCAC4", "").size());
    }

    @Test
    public void objectsReturnedByCoordinatesInTheDistanceRadius() {
        spaceObjectDao.queryObjects("21:30:59.150",
                        "70:49:23.58", "0.1", "",
                        "0", "15", "UCAC4", "")
                .forEach(object -> {
                    try {
                        assertTrue(
                                twoPointsOnSphereInRadius("21:30:59.150", "70:49:23.58",
                                        object.getCatalogRec(), object.getCatalogDec(), 0.1F));
                    } catch (ParseException e) {
                        fail("exception was thrown");
                    }
                });
    }

    @Test
    public void savedObjectsIsReturnedBasedOnCoordinates() throws ParseException {
        spaceObjectDao.saveObject("801-0", "", "UCAC4", "+70°01'57.26\"",
                "21h10m38.791s", Conversions.angleToFloatForm("+70 01 57.26"),
                Conversions.hourAngleToDegrees("21 10 38.791"), 6.81F);
        SpaceObject expectedObject = new SpaceObject("801-0", "", "UCAC4",
                "+70°01'57.26\"", "21h10m38.791s", 6.81F);
        AssertionsForClassTypes.assertThat(spaceObjectDao.queryObjects("21:10:38.791", "70:01:57.26", "0.001", "",
                "0", "15", "UCAC4", "").get(0))
                .usingRecursiveComparison()
                .ignoringFields("id", "fluxes", "numberOfFluxes")
                .isEqualTo(expectedObject);
    }

    private boolean twoPointsOnSphereInRadius(String ra, String dec,
                                              String raRef, String decRef,
                                              Float radius) throws ParseException {
        // helper method to determine angular distance between two points on a sphere
        raRef = raRef.replace('h', ':');
        raRef = raRef.replace('m', ':');
        raRef = raRef.substring(0, raRef.length()-1);
        decRef = decRef.replace('°', ':');
        decRef = decRef.replace('\'', ':');
        decRef = decRef.substring(0, decRef.length()-1);
        float raFloat = Conversions.hourAngleToDegrees(ra);
        float decFloat = Conversions.angleToFloatForm(dec);
        float raRefFloat = Conversions.hourAngleToDegrees(raRef);
        float decRefFloat = Conversions.angleToFloatForm(decRef);
        double distance = acos(sin(decFloat)*sin(decRefFloat)+
                cos(decFloat)*cos(decRefFloat)*cos(raFloat-raRefFloat));
        return distance < radius;
    }
}
