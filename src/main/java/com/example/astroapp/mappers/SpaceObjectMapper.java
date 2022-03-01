package com.example.astroapp.mappers;

import com.example.astroapp.entities.SpaceObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpaceObjectMapper implements RowMapper<SpaceObject> {

    @Override
    public SpaceObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpaceObject spaceObject = new SpaceObject();
        spaceObject.setCatalogID(rs.getString("catalog_id"));
        spaceObject.setCatalog(rs.getString("catalog"));
        spaceObject.setCatalogDec(rs.getString("catalog_dec"));
        spaceObject.setCatalogRec(rs.getString("catalog_rec"));
        spaceObject.setCatalogMag(rs.getFloat("catalog_mag"));
        return spaceObject;
    }
}
