package com.example.astroapp.mappers;

import com.example.astroapp.dto.ObjectFlux;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import static com.example.astroapp.utils.UnitConversions.angleToExpendedForm;
import static com.example.astroapp.utils.UnitConversions.degreesToHourAngle;

public class ObjectFluxCountRowMapper implements RowMapper<ObjectFlux> {

    @Override
    public ObjectFlux mapRow(ResultSet rs, int rowNum) throws SQLException {
        ObjectFlux objectFlux = new ObjectFlux();
        objectFlux.setCatalog(rs.getString("catalog"));
        objectFlux.setCatalogId(rs.getString("catalog_id"));
        objectFlux.setName(rs.getString("name"));
        try {
            objectFlux.setCatalogRec(degreesToHourAngle(rs.getFloat("catalog_rec")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        objectFlux.setCatalogDec(angleToExpendedForm(rs.getFloat("catalog_dec")));
        objectFlux.setCatalogMag(rs.getFloat("catalog_mag"));
        objectFlux.setNumberOfFluxes(rs.getInt("flux_count"));
        return objectFlux;
    }
}
