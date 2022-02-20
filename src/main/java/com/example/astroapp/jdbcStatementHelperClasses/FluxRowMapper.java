package com.example.astroapp.jdbcStatementHelperClasses;

import com.example.astroapp.dto.ObjectFlux;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FluxRowMapper implements RowMapper<ObjectFlux> {

    @Override
    public ObjectFlux mapRow(ResultSet rs, int rowNum) throws SQLException {
        ObjectFlux objectFlux = new ObjectFlux();
        objectFlux.setCatalog(rs.getString("catalog"));
        objectFlux.setCatalogId(rs.getString("catalog_id"));
        objectFlux.setName(rs.getString("name"));
        objectFlux.setCatalogRec(rs.getFloat("catalog_rec"));
        objectFlux.setCatalogDec(rs.getFloat("catalog_dec"));
        objectFlux.setCatalogMag(rs.getFloat("catalog_mag"));
        objectFlux.setNumberOfFluxes(rs.getInt("flux_count"));

        return objectFlux;
    }
}
