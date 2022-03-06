package com.example.astroapp.utils;

import com.example.astroapp.dto.FluxUserTime;
import com.example.astroapp.helper.Night;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        if (hourAngle.contains(".")) {
            dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        }
        // subtracting zero date because of time zone
        Date zeroDate = dateFormat.parse("00:00:00.000");
        Date angleDate = dateFormat.parse(hourAngle.strip().replace(" ", ":"));
        float seconds = (angleDate.getTime() - zeroDate.getTime()) / 1000F;
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

    public static String addAngleSigns(String angle) {
        angle = angle.replaceFirst(" ", "°");
        angle = angle.replaceFirst(" ", "'");
        angle = angle + "\"";
        return angle;
    }

    public static String addHourAngleSigns(String hourAngle) {
        hourAngle = hourAngle.replaceFirst(" ", "h");
        hourAngle = hourAngle.replaceFirst(" ", "m");
        hourAngle = hourAngle + "s";
        return hourAngle;
    }

    public static float convertFluxesToMagnitude(FluxUserTime flux, List<Night> nights) {
        Night correspondingNight = nights.get(nights.indexOf(flux.getNight()));
        String apToBeUsed = correspondingNight.getApToBeUsed();
        String refApToBeUsed = correspondingNight.getRefApToBeUsed();
        String fluxValue;
        String refFluxValue;
        if (apToBeUsed.equals("auto")) {
            fluxValue = flux.getApAuto();
        } else {
            int apToBeUsedInt = Integer.parseInt(apToBeUsed);
            fluxValue = apToBeUsedInt - 1 < flux.getApertures().length ?
                    flux.getApertures()[apToBeUsedInt - 1] : flux.getApAuto();
        }
        if (refApToBeUsed.equals("auto")) {
            refFluxValue = flux.getRefApAuto();
        } else {
            int apToBeUsedInt = Integer.parseInt(refApToBeUsed);
            refFluxValue = apToBeUsedInt - 1 < flux.getRefApertures().length ?
                    flux.getRefApertures()[apToBeUsedInt - 1] : flux.getRefApAuto();
        }
        if (fluxValue.equals("saturated") || refFluxValue.equals("saturated")) {
            return 0F;
        }
        return (float) (-2.5 * Math.log10(Double.parseDouble(fluxValue) / Double.parseDouble(refFluxValue)));
    }

}
