package com.example.astroapp.unit;

import com.example.astroapp.dto.FluxUserTime;
import com.example.astroapp.helper.Night;
import org.junit.Test;

import static com.example.astroapp.utils.Conversions.calculateMagnitudeAndDeviation;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.List;

public class MagnitudeCalculationTest {
    private Night fluxNight = new Night("22-03-2022", "23-03-2022",
            "Rastislav", 0, "auto", "auto");
    private Night secondNight = new Night("24-03-2022", "25-03-2022",
            "Rastislav", 1, "2", "2");
    private List<Night> nights = List.of(fluxNight, secondNight);
    private FluxUserTime flux = new FluxUserTime("05h48m16.611s", "+30°40'10.54", "153919.62",
            502.728F, new String[]{"111633.03", "131936.39", "142455.14"}, new Double[]{341.4, 375.99, 398.648},
            "200067.38", 493.279F, new String[]{"147200.62", "173115.0", "186533.12"},
            new Double[]{390.4, 427.19, 450.648}, null, null, "Rastislav", "108133573258632378409",
            fluxNight, "2022-03-22 20:08:50.301");

    @Test
    public void calculateMagnitudeApAutoTest() {
        calculateMagnitudeAndDeviation(flux, nights);
        assertEquals(-2.5 * Math.log10(153919.62 / 200067.38), flux.getMagnitude(), 0.001);
    }

    @Test
    public void calculateMagnitudeApFirstTest() {
        fluxNight.setApToBeUsed("1");
        fluxNight.setRefApToBeUsed("1");
        calculateMagnitudeAndDeviation(flux, nights);
        assertEquals(-2.5 * Math.log10(111633.03 / 147200.62), flux.getMagnitude(), 0.001);
    }

    @Test
    public void calculateMagnitudeApSecondNightTest() {
        flux.setNight(secondNight);
        flux.setExpMiddle("2022-03-24 20:08:50.301");
        calculateMagnitudeAndDeviation(flux, nights);
        assertEquals(-2.5 * Math.log10(131936.39 / 173115.0), flux.getMagnitude(), 0.001);
    }

    @Test
    public void calculateDeviationApAutoTest() {
        float DEV_CONSTANT = (float) (2.5 / Math.log(10));
        calculateMagnitudeAndDeviation(flux, nights);
        double a = 502.728F / 153919.62;
        double b = 493.279F / 200067.38;
        assertEquals((DEV_CONSTANT * Math.sqrt(a * a + b * b)), flux.getDeviation(), 0.01);
    }

    @Test
    public void calculateDeviationFirstTest() {
        fluxNight.setApToBeUsed("1");
        fluxNight.setRefApToBeUsed("1");
        float DEV_CONSTANT = (float) (2.5 / Math.log(10));
        calculateMagnitudeAndDeviation(flux, nights);
        double a = 341.4F / 111633.03;
        double b = 390.4F / 147200.62;
        assertEquals((DEV_CONSTANT * Math.sqrt(a * a + b * b)), flux.getDeviation(), 0.01);
    }

    @Test
    public void calculateDeviationSecondNightTest() {
        float DEV_CONSTANT = (float) (2.5 / Math.log(10));
        flux.setNight(secondNight);
        flux.setExpMiddle("2022-03-24 20:08:50.301");
        calculateMagnitudeAndDeviation(flux, nights);
        double a = 375.99F / 131936.39;
        double b = 427.19F / 173115.0;
        assertEquals((DEV_CONSTANT * Math.sqrt(a * a + b * b)), flux.getDeviation(), 0.01);
    }


}
