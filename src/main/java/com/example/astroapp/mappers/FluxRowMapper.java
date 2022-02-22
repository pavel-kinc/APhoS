package com.example.astroapp.mappers;

import com.example.astroapp.dto.FluxUserTime;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.example.astroapp.utils.UnitConversions.angleToExpendedForm;
import static com.example.astroapp.utils.UnitConversions.degreesToHourAngle;

public class FluxRowMapper implements RowMapper<FluxUserTime> {

    @Override
    public FluxUserTime mapRow(ResultSet rs, int rowNum) throws SQLException {
        FluxUserTime fluxUserTime = new FluxUserTime();
        try {
            fluxUserTime.setRA(degreesToHourAngle(rs.getFloat("rec")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fluxUserTime.setDec(angleToExpendedForm(rs.getFloat("dec")));
        Float apAuto = rs.getFloat("ap_auto");
        String apAutoStr = apAuto.equals(0.0f) ? "saturated" : apAuto.toString();
        fluxUserTime.setApAuto(apAutoStr);
        Double[] aperturesArray = (Double[]) rs.getArray("apertures").getArray();
        String[] aperturesStr = Arrays.stream(aperturesArray)
                .map(ap -> ap.equals(0.0d) ? "saturated" : ap.toString())
                .toArray(String[]::new);
        fluxUserTime.setApertures(aperturesStr);
        fluxUserTime.setUsername(rs.getString("username"));
        fluxUserTime.setExpBegin(rs.getTimestamp("exposure_begin"));
        fluxUserTime.setExpEnd(rs.getTimestamp("exposure_end"));
        fluxUserTime.setUserId(rs.getString("google_sub"));
        return fluxUserTime;
    }
}
