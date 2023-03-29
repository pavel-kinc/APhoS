package cz.muni.aphos.openapi.models;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoordinatesUnitTests {

    @Test
    public void coordinatesExampleTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Coordinates coords = mapper.readValue(Coordinates.example, Coordinates.class);
        assertTrue(coords.isValid());
        coords.setRadius(0.6);
        assertTrue(coords.isValid());
    }

    @Test
    public void coordinatesCorrectTest() throws JsonProcessingException {
        Coordinates coords = new Coordinates("21:34:55.125", "71:25:14.123", 0.05);
        assertTrue(coords.isValid());
        Coordinates coords2 = new Coordinates("7:34:55.12512", "-71:25:14", 2.0);
        assertTrue(coords2.isValid());
    }

    @Test
    public void incorrectRadius_coordinatesTest() throws JsonProcessingException {
        assertThrows(ValidationException.class, () -> new Coordinates("21:34:55.125", "71:25:14.123", -0.05));
        Coordinates coords = new Coordinates("21:34:55.125", "71:25:14.123", 0.05);
        assertThrows(ValidationException.class, () -> coords.setRadius(-1.0));
    }

    @Test
    public void incorrectPatterns_coordinatesTest() throws JsonProcessingException {
        // patterns for ascension and declination are similar, declination can start with +, or -

        // 3 nums in RA
        assertThrows(ValidationException.class, () -> new Coordinates("21:354:55.125", "71:25:14.123", 0.05));

        // +- in declination
        assertThrows(ValidationException.class, () -> new Coordinates("21:34:55.125", "+-71:25:14.123", 0.05));

        // 1 num in RA seconds
        assertThrows(ValidationException.class, () -> new Coordinates("21:45:5.125", "71:25:14.123", 0.05));

        // missing ':' in declination
        assertThrows(ValidationException.class, () -> new Coordinates("21:34:55.125", "71:25 14.123", 0.05));
    }
}
