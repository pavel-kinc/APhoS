package cz.muni.aphos.mappers;

import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.PhotoProperties;
import cz.muni.aphos.helper.Night;
import cz.muni.aphos.openapi.models.Flux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * The Flux row mapper. Handles the mapping of the rows from the query for fluxes of the given objects.
 * Maps the flux values and the reference object flux values, the time of the exposure, the
 * uploading user, and the Night object for the night to an object of FluxUserTime type.
 */
public class FluxApiMapper implements RowMapper<Flux> {

    private static final Logger log = LoggerFactory.getLogger(FluxRowMapper.class);

    @Override
    public Flux mapRow(ResultSet rs, int rowNum) throws SQLException {
        Flux flux = new Flux();
        flux.setRightAsc(rs.getString("rec"));
        flux.setDeclination(rs.getString("dec"));
        flux.setAddedBy(rs.getString("username"));
        flux.setApAuto(rs.getDouble("apAuto"));
        Double[] aperturesArray = (Double[]) rs.getArray("apertures").getArray();
        flux.setApertures(List.of(aperturesArray));
        PhotoProperties photo = new PhotoProperties();
        photo.setExposureBegin(rs.getTimestamp("exposure_begin"));
        photo.setExposureEnd(rs.getTimestamp("exposure_end"));
        flux.setPhoto(photo);
        return flux;
    }
}
