package com.example.astroapp;


import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static com.example.astroapp.utils.UnitConversions.angleToFloatForm;
import static com.example.astroapp.utils.UnitConversions.hourAngleToDegrees;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionsTest {

    @Test
    public void convertBasicRAtoDegTest() throws ParseException {
        double expected = 325.4792;
        assertEquals(expected, hourAngleToDegrees("21 41 55.291"), 0.001);
    }

    @Test
    public void convertIncorrectInputTest() {
        assertThrows(ParseException.class, () -> hourAngleToDegrees("a21 41 55.291"));
    }

    @Test
    public void convertBasicDecTest() {
        assertEquals(71.3114222, angleToFloatForm("+71 18 41.12"), 0.001);
    }

    @Test
    public void convertMinusDecTest() {
        assertEquals(-71.3114222, angleToFloatForm("-71 18 41.12"), 0.001);
    }

    @Test
    public void convertIncorrectInputDecTest() {
        assertThrows(NumberFormatException.class, () -> angleToFloatForm("a-21 41 55.291"));

    }
}
