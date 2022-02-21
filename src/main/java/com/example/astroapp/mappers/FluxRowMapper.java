package com.example.astroapp.mappers;

import com.example.astroapp.dto.FluxUserTime;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

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
        fluxUserTime.setApAuto(rs.getFloat("ap_auto"));
        fluxUserTime.setApertures((Double[]) rs.getArray("apertures").getArray());
        fluxUserTime.setUsername(rs.getString("username"));
        fluxUserTime.setExpBegin(rs.getTimestamp("exposure_begin"));
        fluxUserTime.setExpEnd(rs.getTimestamp("exposure_end"));
        return fluxUserTime;
    }
}
