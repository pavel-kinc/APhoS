package cz.muni.var4astro.mappers;

import cz.muni.var4astro.dto.ObjectFluxCount;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectFluxCountRowMapper implements RowMapper<ObjectFluxCount> {

    @Override
    public ObjectFluxCount mapRow(ResultSet rs, int rowNum) throws SQLException {
        ObjectFluxCount objectFluxCount = new ObjectFluxCount();
        objectFluxCount.setId(rs.getLong("obj_id"));
        objectFluxCount.setCatalog(rs.getString("catalog"));
        objectFluxCount.setCatalogId(rs.getString("catalog_id"));
        objectFluxCount.setName(rs.getString("name"));
        objectFluxCount.setCatalogRec(rs.getString("catalog_rec"));
        objectFluxCount.setCatalogDec(rs.getString("catalog_dec"));
        objectFluxCount.setCatalogMag(rs.getFloat("catalog_mag"));
        objectFluxCount.setNumberOfFluxes(rs.getInt("flux_count"));
        return objectFluxCount;
    }
}
