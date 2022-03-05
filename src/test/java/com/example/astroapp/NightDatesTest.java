package com.example.astroapp;

import com.example.astroapp.helper.Night;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NightDatesTest {

    @Test
    void twoDatesFromSameMorningTest() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestampFirst = new Timestamp(simpleDateFormat
                .parse("2021-11-07 01:55:15.709").getTime());
        Timestamp timestampSnd = new Timestamp(simpleDateFormat
                .parse("2021-11-07 11:55:15.709").getTime());
        assertEquals(new Night(timestampFirst, "rasto"),
                new Night(timestampSnd, "rasto"));
    }

    @Test
    void twoDatesNightAndMorningTest() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestampFirst = new Timestamp(simpleDateFormat
                .parse("2021-11-06 22:55:15.709").getTime());
        Timestamp timestampSnd = new Timestamp(simpleDateFormat
                .parse("2021-11-07 11:55:15.709").getTime());
        assertEquals(new Night(timestampFirst, "rasto"),
                new Night(timestampSnd, "rasto"));
    }

    @Test
    void sameNightTest() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestampFirst = new Timestamp(simpleDateFormat
                .parse("2021-11-06 22:55:15.709").getTime());
        Timestamp timestampSnd = new Timestamp(simpleDateFormat
                .parse("2021-11-06 18:55:15.709").getTime());
        assertEquals(new Night(timestampFirst, "rasto"),
                new Night(timestampSnd, "rasto"));
    }

    @Test
    void sameNightDifferentUsers() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestampFirst = new Timestamp(simpleDateFormat
                .parse("2021-11-06 22:55:15.709").getTime());
        Timestamp timestampSnd = new Timestamp(simpleDateFormat
                .parse("2021-11-06 18:55:15.709").getTime());
        assertNotEquals(new Night(timestampFirst, "rasto"),
                new Night(timestampSnd, "rasto1"));
    }

    @Test
    void twoMorningsDifferentDaysTest() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestampFirst = new Timestamp(simpleDateFormat
                .parse("2021-11-08 01:55:15.709").getTime());
        Timestamp timestampSnd = new Timestamp(simpleDateFormat
                .parse("2021-11-07 11:55:15.709").getTime());
        assertNotEquals(new Night(timestampFirst, "rasto"),
                new Night(timestampSnd, "rasto"));
    }

    @Test
    void sameDayDifferentNightsTest() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestampFirst = new Timestamp(simpleDateFormat
                .parse("2021-11-06 22:55:15.709").getTime());
        Timestamp timestampSnd = new Timestamp(simpleDateFormat
                .parse("2021-11-06 7:55:15.709").getTime());
        assertNotEquals(new Night(timestampFirst, "rasto"),
                new Night(timestampSnd, "rasto"));
    }

    @Test
    void sameDayEdgeCaseTest() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestampFirst = new Timestamp(simpleDateFormat
                .parse("2021-11-06 12:01:15.709").getTime());
        Timestamp timestampSnd = new Timestamp(simpleDateFormat
                .parse("2021-11-06 11:59:15.709").getTime());
        assertNotEquals(new Night(timestampFirst, "rasto"),
                new Night(timestampSnd, "rasto"));
    }

    @Test
    void nightObjectConstructionTest() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse("2021-11-06");
        Date date2 = simpleDateFormat.parse("2021-11-07");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp timestampFirst = new Timestamp(simpleDateFormat
                .parse("2021-11-06 22:55:15.709").getTime());
        Night night = new Night(timestampFirst, "rasto");
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        night.setFirstDateOfTheNight(simpleDateFormat.format(date1));
        night.setSecondDateOfTheNight(simpleDateFormat.format(date2));
        assertEquals(new Night(timestampFirst, "rasto"), night);
    }

}
