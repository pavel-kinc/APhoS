package com.example.astroapp.mappers;

import com.example.astroapp.dto.FluxUserTime;
import com.example.astroapp.helper.Night;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;

import static com.example.astroapp.utils.UnitConversions.convertFluxesToMagnitude;

public class FluxRowMapper implements RowMapper<FluxUserTime> {

    @Override
    public FluxUserTime mapRow(ResultSet rs, int rowNum) throws SQLException {
        FluxUserTime fluxUserTime = new FluxUserTime();
        fluxUserTime.setRightAsc(rs.getString("rec"));
        fluxUserTime.setDec(rs.getString("dec"));
        Float apAuto = rs.getFloat("ap_auto");
        String apAutoStr = apAuto.equals(0.0f) ? "saturated" : apAuto.toString();
        fluxUserTime.setApAuto(apAutoStr);
        Double[] aperturesArray = (Double[]) rs.getArray("apertures").getArray();
        String[] aperturesStr = Arrays.stream(aperturesArray)
                .map(ap -> ap.equals(0.0d) ? "saturated" : ap.toString())
                .toArray(String[]::new);
        fluxUserTime.setApertures(aperturesStr);
        Float refApAuto = rs.getFloat("ref_ap_auto");
        String refApAutoStr = refApAuto.equals(0.0f) ? "saturated" : refApAuto.toString();
        fluxUserTime.setRefApAuto(refApAutoStr);
        Double[] refAperturesArray = (Double[]) rs.getArray("ref_apertures").getArray();
        String[] refAperturesStr = Arrays.stream(refAperturesArray)
                .map(ap -> ap.equals(0.0d) ? "saturated" : ap.toString())
                .toArray(String[]::new);
        fluxUserTime.setRefApertures(refAperturesStr);
        fluxUserTime.setUsername(rs.getString("username"));
        fluxUserTime.setExpBegin(rs.getTimestamp("exposure_begin").toString());
        fluxUserTime.setExpEnd(rs.getTimestamp("exposure_end").toString());
        try {
            Night night = new Night(rs.getTimestamp("exposure_begin"),
                    fluxUserTime.getUsername());
            fluxUserTime.setNight(night);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fluxUserTime.setUserId(rs.getString("google_sub"));
        return fluxUserTime;
    }
}
