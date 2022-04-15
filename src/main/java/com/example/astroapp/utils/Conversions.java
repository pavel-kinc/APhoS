package com.example.astroapp.utils;

import com.example.astroapp.dto.FluxUserTime;
import com.example.astroapp.helper.Night;
import com.example.astroapp.exceptions.IllegalCoordinateFormatException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Util class for converting units.
 */
public class Conversions {

    /**
     * The constant DEV_CONSTANT.
     */
    public static float DEV_CONSTANT = (float) (2.5 / Math.log(10));

    /**
     * Hour angle to degrees in float value. Primarily for converting Right ascension.
     *
     * @param hourAngle the angle in the format "HH MM SS"
     * @return the angle in degrees
     * @throws ParseException the parse exception
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
            throw new IllegalCoordinateFormatException(e);
        }
    }

    /**
     * Add degree, minute and seconds signs for presenting.
     *
     * @param angle the angle
     * @return the string
     */
    public static String addAngleSigns(String angle) {
        angle = angle.replaceFirst(" ", "°");
        angle = angle.replaceFirst(" ", "'");
        angle = angle + "\"";
        return angle;
    }

    /**
     * Add hour, minute, second characters.
     *
     * @param hourAngle the hour angle
     * @return the string
     */
    public static String addHourAngleSigns(String hourAngle) {
        hourAngle = hourAngle.replaceFirst(" ", "h");
        hourAngle = hourAngle.replaceFirst(" ", "m");
        hourAngle = hourAngle + "s";
        return hourAngle;
    }

    /**
     * Calculate magnitude and deviation. On the page user chooses an aperture value
     * to be used for each night. These values are stored in the nights list. Each flux has
     * a night object representing the night it comes from. Its night is found in the nights
     * list and the apertures are set according to the values chosen by user for that night.
     *
     * @param flux   the flux to calculate magnitude and deviation on
     * @param nights the night objects with aperture to be used set by the user
     */
    public static void calculateMagnitudeAndDeviation(FluxUserTime flux, List<Night> nights) {
        Night correspondingNight = nights.get(nights.indexOf(flux.getNight()));
        String apToBeUsed = correspondingNight.getApToBeUsed();
        String refApToBeUsed = correspondingNight.getRefApToBeUsed();
        String fluxValue;
        String refFluxValue;
        double devValue;
        double refDevValue;
        if (apToBeUsed.equals("auto")) {
            fluxValue = flux.getApAuto();
            devValue = flux.getApAutoDev();
        } else {
            int apToBeUsedInt = Integer.parseInt(apToBeUsed);
            fluxValue = apToBeUsedInt - 1 < flux.getApertures().length ?
                    flux.getApertures()[apToBeUsedInt - 1] : flux.getApAuto();
            devValue = apToBeUsedInt - 1 < flux.getApertureDevs().length ?
                    flux.getApertureDevs()[apToBeUsedInt - 1] : flux.getApAutoDev();
        }
        if (refApToBeUsed.equals("auto")) {
            refFluxValue = flux.getRefApAuto();
            refDevValue = flux.getRefApAutoDev();
        } else {
            int apToBeUsedInt = Integer.parseInt(refApToBeUsed);
            refFluxValue = apToBeUsedInt - 1 < flux.getRefApertures().length ?
                    flux.getRefApertures()[apToBeUsedInt - 1] : flux.getRefApAuto();
            refDevValue = apToBeUsedInt - 1 < flux.getRefApertureDevs().length ?
                    flux.getRefApertureDevs()[apToBeUsedInt - 1] : flux.getRefApAutoDev();
        }
        if (fluxValue.equals("saturated") || refFluxValue.equals("saturated")) {
            flux.setMagnitude(Float.NEGATIVE_INFINITY);
        } else {
            // magnitude calculation
            float fluxFloat = Float.parseFloat(fluxValue);
            float fluxRefFloat = Float.parseFloat(refFluxValue);
            flux.setMagnitude((float) (-2.5 * Math.log10(fluxFloat / fluxRefFloat)));
            if (devValue == 0 || refDevValue == 0) {
                flux.setDeviation(0F);
            } else {
                // standard deviation calculation
                double a = devValue / fluxFloat;
                double b = refDevValue / fluxRefFloat;
                flux.setDeviation((float) (DEV_CONSTANT * Math.sqrt(a * a + b * b)));
            }
        }
    }

}
