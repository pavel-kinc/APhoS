package com.example.astroapp.mappers;

import com.example.astroapp.dto.FluxUserTime;
import com.example.astroapp.helper.Night;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class FluxRowMapper implements RowMapper<FluxUserTime> {

    @Override
    public FluxUserTime mapRow(ResultSet rs, int rowNum) throws SQLException {
        FluxUserTime fluxUserTime = new FluxUserTime();
        fluxUserTime.setRightAsc(rs.getString("rec"));
        fluxUserTime.setDec(rs.getString("dec"));
        Float apAuto = rs.getFloat("ap_auto");
        String apAutoStr = apAuto.equals(0.0f) ? "saturated" : apAuto.toString();
        fluxUserTime.setApAuto(apAutoStr);
        fluxUserTime.setApAutoDev(rs.getFloat("ap_auto_dev"));
        Double[] aperturesArray = (Double[]) rs.getArray("apertures").getArray();
        String[] aperturesStr = Arrays.stream(aperturesArray)
                .map(ap -> ap.equals(0.0d) ? "saturated" : ap.toString())
                .toArray(String[]::new);
        fluxUserTime.setApertures(aperturesStr);
        fluxUserTime.setApertureDevs((Double[]) rs.getArray("aperture_devs").getArray());
        Float refApAuto = rs.getFloat("ref_ap_auto");
        String refApAutoStr = refApAuto.equals(0.0f) ? "saturated" : refApAuto.toString();
        fluxUserTime.setRefApAuto(refApAutoStr);
        fluxUserTime.setRefApAutoDev(rs.getFloat("ref_ap_auto_dev"));
        Double[] refAperturesArray = (Double[]) rs.getArray("ref_apertures").getArray();
        String[] refAperturesStr = Arrays.stream(refAperturesArray)
                .map(ap -> ap.equals(0.0d) ? "saturated" : ap.toString())
                .toArray(String[]::new);
        fluxUserTime.setRefApertures(refAperturesStr);
        fluxUserTime.setRefApertureDevs((Double[]) rs.getArray("ref_ap_devs").getArray());
        fluxUserTime.setUsername(rs.getString("username"));
        long expBegin = rs.getTimestamp("exposure_begin").getTime();
        long expEnd = rs.getTimestamp("exposure_end").getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        fluxUserTime.setExpMiddle(dateFormatter.format(new Date((expBegin + expEnd) / 2)));
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
