package com.example.astroapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Util class for converting units.
 */
public class UnitConversions {


    /**
     * Hour angle to degrees in float value.
     *
     * @param hourAngle the angle in the format "HH MM SS"
     * @return the angle in degrees
     */
    public static float hourAngleToDegrees(String hourAngle) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date zeroDate = dateFormat.parse("00:00:00");
        Date myValue = dateFormat.parse(hourAngle.strip().replace(" ", ":"));
        float seconds = (myValue.getTime() - zeroDate.getTime());
        return seconds / (24*3600);
    }


    /**
     * Angle to float form.
     *
     * @param angle the angle in the format Degrees Minutes Seconds
     * @return the float form of the angle in degrees
     */
    public static float angleToFloatForm(String angle) {
        try {
            String[] angleArray = angle.strip().split(" ");
            float minutes = Float.parseFloat(angleArray[1]) * 60 + Float.parseFloat(angleArray[2]);
            return Float.parseFloat(angleArray[0]) + minutes / 3600;
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
