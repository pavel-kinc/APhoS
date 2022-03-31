package com.example.astroapp.dao;

import com.example.astroapp.dto.PhotoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
@Transactional
public class PhotoPropertiesDao extends JdbcDaoSupport {

    @Autowired
    PhotoPropertiesDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public void savePhotoProps(PhotoProperties photoProperties) {
        assert getJdbcTemplate() != null;
        String insertQuery = "INSERT INTO photo_properties " +
                "(id, exposure_begin, exposure_end)" +
                "VALUES (nextval('photo_properties_id_seq'), ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{"id"});
            ps.setTimestamp(1, photoProperties.getExposureBegin());
            ps.setTimestamp(2, photoProperties.getExposureEnd());
            return ps;
        }, keyHolder);
        photoProperties.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

}
