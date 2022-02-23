package com.example.astroapp.mappers;

import com.example.astroapp.dto.ObjectFlux;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import static com.example.astroapp.utils.UnitConversions.addAngleSigns;
import static com.example.astroapp.utils.UnitConversions.addHourAngleSigns;

public class ObjectFluxCountRowMapper implements RowMapper<ObjectFlux> {

    @Override
    public ObjectFlux mapRow(ResultSet rs, int rowNum) throws SQLException {
        ObjectFlux objectFlux = new ObjectFlux();
        objectFlux.setId(rs.getLong("obj_id"));
        objectFlux.setCatalog(rs.getString("catalog"));
        objectFlux.setCatalogId(rs.getString("catalog_id"));
        objectFlux.setName(rs.getString("name"));
        objectFlux.setCatalogRec(rs.getString("catalog_rec"));
        objectFlux.setCatalogDec(rs.getString("catalog_dec"));
        objectFlux.setCatalogMag(rs.getFloat("catalog_mag"));
        objectFlux.setNumberOfFluxes(rs.getInt("flux_count"));
        return objectFlux;
    }
}
