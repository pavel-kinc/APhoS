package com.example.astroapp.dao;

import com.example.astroapp.dto.ObjectFluxes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class SpaceObjectDao extends JdbcDaoSupport {


    @Autowired
    SpaceObjectDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public List<ObjectFluxes> queryObjects(String RA, String dec, String radius, String name, String minMag,
                                            String maxMag, String catalog, String objectId) {
        StringBuilder query = new StringBuilder("SELECT * FROM object LEFT OUTER JOIN flux WHERE ");
        boolean appendAnd = false;
        if (!RA.isEmpty()) {
            query.append("earth_box(ll_to_earth(?, ?), ?) @> ll_to_earth(catalog_dec, catalog_rec)");
            appendAnd = true;
        }
        if (!name.isEmpty()) {
            if (appendAnd) {
                query.append(" AND");
            }
            query.append(" name LIKE ?");
            appendAnd = true;
        }
        if (!(minMag.equals("0") && (maxMag.equals("15")))) {
            if (appendAnd) {
                query.append(" AND");
            }
            query.append(" catalog_mag BETWEEN ? AND ?");
            appendAnd = true;
        }

        if (!catalog.isEmpty()) {
            if (appendAnd) {
                query.append(" AND");
            }
            query.append(" catalog LIKE ?");
            appendAnd = true;
        }

        if (!objectId.isEmpty()) {
            if (appendAnd) {
                query.append(" AND");
            }
            query.append(" object.id LIKE ?");
        }
        String finishedQuery = query.toString();
        return null;
    }

    public long saveObject(String catalogId, String name, String catalog,
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
            ps.setFloat(4, catalogRec);
            ps.setFloat(5, catalogDec);
            ps.setFloat(6, catalogDec);
            ps.setFloat(7, catalogRec);
            ps.setFloat(8, catalogMag);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<Long> getObjectsByCatId(String catalogId) {
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().queryForList("SELECT id FROM object " +
                "WHERE catalog_id=?", Long.class, catalogId);
    }
}
