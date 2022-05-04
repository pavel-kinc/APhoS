package cz.muni.var4astro.mappers;

import cz.muni.var4astro.dto.FluxUserTime;
import cz.muni.var4astro.helper.Night;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class FluxRowMapper implements RowMapper<FluxUserTime> {

    private static final Logger log = LoggerFactory.getLogger(FluxRowMapper.class);

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
        Array arr = rs.getArray("aperture_devs");
        List<Double> zeroList = new ArrayList<>();
        for (int i = 0; i < aperturesArray.length; i++) {
            zeroList.add(0D);
        }
        if (arr != null) {
            fluxUserTime.setApertureDevs((Double[]) arr.getArray());
        } else {
            fluxUserTime.setApertureDevs(zeroList.toArray(new Double[]{}));
        }
        Float refApAuto = rs.getFloat("ref_ap_auto");
        String refApAutoStr = refApAuto.equals(0.0f) ? "saturated" : refApAuto.toString();
        fluxUserTime.setRefApAuto(refApAutoStr);
        fluxUserTime.setRefApAutoDev(rs.getFloat("ref_ap_auto_dev"));
        Double[] refAperturesArray = (Double[]) rs.getArray("ref_apertures").getArray();
        String[] refAperturesStr = Arrays.stream(refAperturesArray)
                .map(ap -> ap.equals(0.0d) ? "saturated" : ap.toString())
                .toArray(String[]::new);
        fluxUserTime.setRefApertures(refAperturesStr);
        Array arrRef = rs.getArray("ref_ap_devs");
        if (arr != null) {
            fluxUserTime.setRefApertureDevs((Double[]) arrRef.getArray());
        } else {
            fluxUserTime.setRefApertureDevs(zeroList.toArray(new Double[]{}));
        }
        fluxUserTime.setUsername(rs.getString("username"));
        long expBegin = rs.getTimestamp("exposure_begin").getTime();
        long expEnd = rs.getTimestamp("exposure_end").getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fluxUserTime.setExpMiddle(dateFormatter.format(new Date((expBegin + expEnd) / 2)));
        try {
            Night night = new Night(rs.getTimestamp("exposure_begin"),
                    fluxUserTime.getUsername());
            fluxUserTime.setNight(night);
        } catch (ParseException e) {
            log.error("Night date was unable to be parsed", e);
        }
        fluxUserTime.setUserId(rs.getString("google_sub"));
        return fluxUserTime;
    }
}
