package com.example.astroapp.dao;

import com.example.astroapp.dto.ObjectFlux;
import com.example.astroapp.mappers.ObjectFluxCountRowMapper;
import com.example.astroapp.mappers.ObjectPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional
public class SpaceObjectDao extends JdbcDaoSupport {


    @Autowired
    SpaceObjectDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public List<ObjectFlux> queryObjects(String RA, String dec, String radius, String name, String minMag,
                                         String maxMag, String catalog, String objectId) {
        StringBuilder query = new StringBuilder("SELECT object.id AS obj_id, name, catalog, catalog_id, catalog_rec, catalog_dec, " +
                "catalog_mag, count(flux.id) AS flux_count" +
                " FROM object LEFT OUTER JOIN flux ON object_id=object.id");
        boolean appendAnd = false;
        if (!RA.isEmpty()) {
            query.append(" WHERE");
            query.append(" earth_box(ll_to_earth(?, ?), ?) @> object.coordinates");
            appendAnd = true;
        }
        if (!name.isEmpty()) {
            if (appendAnd) {
                query.append(" AND");
            } else {
                query.append(" WHERE");
            }
            query.append(" name LIKE ?");
            appendAnd = true;
        }
        if (!(minMag.equals("0") && (maxMag.equals("15")))) {
            if (appendAnd) {
                query.append(" AND");
            } else {
                query.append(" WHERE");
            }
            query.append(" catalog_mag BETWEEN ? AND ?");
            appendAnd = true;
        }

        if (!catalog.equals("All catalogues")) {
            if (appendAnd) {
                query.append(" AND");
            } else {
                query.append(" WHERE");
            }
            query.append(" catalog LIKE ?");
            appendAnd = true;
        }

        if (!objectId.isEmpty()) {
            if (appendAnd) {
                query.append(" AND");
            } else {
                query.append(" WHERE");
            }
            query.append(" catalog_id LIKE ?");
        }
        query.append(" GROUP BY object.id, name, catalog, catalog_id, catalog_rec, catalog_dec, catalog_mag LIMIT 100");
        String finishedQuery = query.toString();
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(finishedQuery, new ObjectPreparedStatementSetter(
                RA, dec, radius, name, minMag, maxMag, catalog, objectId), new ObjectFluxCountRowMapper());
    }

    public long saveObject(String catalogId, String name, String catalog, String strDec, String strRec,
                           Float catalogDec, Float catalogRec, Float catalogMag) {
        assert getJdbcTemplate() != null;
        List<Long> existingIds = getJdbcTemplate().queryForList("SELECT id FROM object " +
                "WHERE catalog_id=?", Long.class, catalogId);
        if (existingIds.size() != 0) {
            return existingIds.get(0);
        }
        String insertQuery = "INSERT INTO object " +
                "(id, name, catalog, catalog_id, catalog_rec, catalog_dec, coordinates, catalog_mag)" +
                "VALUES (nextval('object_id_seq'), ?, ?, ?, ?, ?, ll_to_earth(?, ?), ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, catalog);
            ps.setString(3, catalogId);
            ps.setString(4, strRec);
            ps.setString(5, strDec);
            ps.setFloat(6, catalogDec);
            ps.setFloat(7, catalogRec);
            ps.setFloat(8, catalogMag);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<String> getAvailableCatalogues() {
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().queryForList("SELECT DISTINCT catalog FROM object", String.class);
    }

    public List<Long> getObjectsByCatId(String catalogId) {
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().queryForList("SELECT id FROM object " +
                "WHERE catalog_id=?", Long.class, catalogId);
    }
}
