package com.example.astroapp.dao;

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
public class SpaceObjectDao extends JdbcDaoSupport {


    @Autowired
    SpaceObjectDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }
    @Retryable(backoff = @Backoff(delay = 100, maxDelay = 300))
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public long saveObject(String catalogId, String name, String catalog,
                           Float catalogDec, Float catalogRec, Float catalogMag) {
        assert getJdbcTemplate() != null;
        List<Long> existingIds = getJdbcTemplate().queryForList("SELECT id FROM object " +
                "WHERE catalog_id=?", Long.class, catalogId);
        if (existingIds.size() != 0) {
            return existingIds.get(0);
        }
        String insertQuery = "INSERT INTO object " +
                "(id, name, catalog, catalog_id, catalog_rec, catalog_dec, catalog_mag)" +
                "VALUES (nextval('object_id_seq'), ?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, catalog);
            ps.setString(3, catalogId);
            ps.setFloat(4, catalogDec);
            ps.setFloat(5, catalogRec);
            ps.setFloat(6, catalogMag);
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
