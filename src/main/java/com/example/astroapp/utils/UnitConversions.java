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
     * Hour angle to degrees in float value. Primarily for converting Right ascension.
     *
     * @param hourAngle the angle in the format "HH MM SS"
     * @return the angle in degrees
     */
    public static float hourAngleToDegrees(String hourAngle) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        // subtracting zero date because of time zone
        Date zeroDate = dateFormat.parse("00:00:00.000");
        Date myValue = dateFormat.parse(hourAngle.strip().replace(" ", ":"));
        float seconds = (myValue.getTime() - zeroDate.getTime()) / 1000F;
        return seconds / 3600 * 15;
    }


    /**
     * Angle to float form. Primarily for converting Declination.
     *
     * @param angle the angle in the format Degrees Minutes Seconds
     * @return the float form of the angle in degrees
     */
    public static float angleToFloatForm(String angle) {
        try {
            String[] angleArray = angle.strip().replace(":", " ").split(" ");
            float seconds = Float.parseFloat(angleArray[1]) * 60 + Float.parseFloat(angleArray[2]);
            float degrees = Float.parseFloat(angleArray[0]);
            return degrees > 0 ? degrees + seconds / 3600 : degrees - seconds / 3600;
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String angleToExpendedForm(Float angle) {
        String sign = "+";
        if (angle < 0) {
            sign = "-";
            angle = -angle;
        }
        String degrees = Integer.toString(angle.intValue());
        int minutes = (int) ((angle - angle.intValue()) * 60);
        float seconds = ((angle - angle.intValue()) * 3600) - (minutes * 60);
        return sign + String.format("%s°%d'%.2f\"", degrees, minutes, seconds);
    }

    public static String degreesToHourAngle(Float angle) throws ParseException {
        float seconds = angle * 3600 / 15;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date zeroDate = dateFormat.parse("00:00:00.000");
        // adding zero date because of time zone
        String hourAngle = dateFormat.format((long) (seconds*1000) + zeroDate.getTime());
        hourAngle = hourAngle.replaceFirst(":", "h");
        hourAngle = hourAngle.replaceFirst(":", "m");
        hourAngle = hourAngle+"s";
        return hourAngle;
    }

}
