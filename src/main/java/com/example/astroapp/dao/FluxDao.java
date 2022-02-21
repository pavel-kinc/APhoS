package com.example.astroapp.dao;

import com.example.astroapp.dto.FluxUserTime;
import com.example.astroapp.mappers.FluxRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional
public class FluxDao extends JdbcDaoSupport {


    @Autowired
    FluxDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public long saveFlux(Float rec, Float dec, Float apertureAuto, Long spaceObjectId,
                         String userId, Long photoId, Float[] apertures) {
        assert getJdbcTemplate() != null;
        String insertQuery = "INSERT INTO flux " +
                "(id, rec, dec, coordinates, ap_auto, object_id, user_id, photo_properties_id, apertures)" +
                "VALUES (nextval('flux_id_seq'), ?, ?, ll_to_earth(?, ?) ,?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{"id"});
            ps.setFloat(1, rec);
            ps.setFloat(2, dec);
            ps.setFloat(3, dec);
            ps.setFloat(4, rec);
            ps.setFloat(5, apertureAuto);
            ps.setLong(6, spaceObjectId);
            ps.setString(7, userId);
            ps.setLong(8, photoId);
            ps.setArray(9, connection.createArrayOf("float8", apertures));
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<FluxUserTime> getFluxesByObjId(Long objectId) {
        assert getJdbcTemplate() != null;
        String query = "SELECT rec, dec, ap_auto, apertures, username, exposure_begin, exposure_end " +
                "FROM (flux LEFT OUTER JOIN users ON users.google_sub = user_id) " +
                "LEFT OUTER JOIN photo_properties ON photo_properties_id = photo_properties.id " +
                "WHERE object_id = ?";
        return getJdbcTemplate().query(query, new FluxRowMapper(), objectId);
    }

}
