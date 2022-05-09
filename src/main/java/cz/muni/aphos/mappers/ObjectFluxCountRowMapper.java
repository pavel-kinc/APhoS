package cz.muni.aphos.mappers;

import cz.muni.aphos.dto.ObjectFluxCount;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The ObjectFluxCount row mapper. Maps the rows from the query for a space_object
 * and count of its fluxes and saves them to a ObjectFluxCount object.
 *
 */
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
