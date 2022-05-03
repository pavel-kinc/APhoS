package cz.muni.var4astro.unit;


import cz.muni.var4astro.exceptions.IllegalCoordinateFormatException;
import cz.muni.var4astro.utils.Conversions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionsTests {

    @Test
    public void convertBasicRAtoDegTest() throws ParseException {
        double expected = 325.4792;
        Assertions.assertEquals(expected, Conversions.hourAngleToDegrees("21 41 55.291"), 0.01);
    }

    @Test
    public void convertBasicRAtoDegOneDecimalTest() throws ParseException {
        double expected = 325.4792;
        Assertions.assertEquals(expected, Conversions.hourAngleToDegrees("21 41 55.2"), 0.01);
    }

    @Test
    public void convertBasicRAtoDegTestNoDecimalTest() throws ParseException {
        double expected = 325.4792;
        Assertions.assertEquals(expected, Conversions.hourAngleToDegrees("21 41 55"), 0.01);
    }

    @Test
    public void convertBasicRAtoDegWithColonTest() throws ParseException {
        double expected = 325.4792;
        Assertions.assertEquals(expected, Conversions.hourAngleToDegrees("21:41:55.291"), 0.01);
    }

    @Test
    public void convertIncorrectInputTest() {
        assertThrows(ParseException.class, () -> Conversions.hourAngleToDegrees("a21 41 55.291"));
    }

    @Test
    public void convertIncorrectDecMissingDegreesTest() {
        assertThrows(IllegalCoordinateFormatException.class, () -> Conversions.angleToFloatForm("41 55.291"));
    }

    @Test
    public void convertBasicDecTest() {
        Assertions.assertEquals(71.3114222, Conversions.angleToFloatForm("+71 18 41.12"), 0.001);
    }

    @Test
    public void convertBasicDecWithColonTest() {
        Assertions.assertEquals(71.3114222, Conversions.angleToFloatForm("+71:18:41.12"), 0.001);
    }

    @Test
    public void convertMinusDecTest() {
        Assertions.assertEquals(-71.3114222, Conversions.angleToFloatForm("-71 18 41.12"), 0.001);
    }

    @Test
    public void convertIncorrectInputDecTest() {
        assertThrows(NumberFormatException.class, () -> Conversions.angleToFloatForm("a-21 41 55.291"));

    }
}
