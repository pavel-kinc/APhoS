package cz.muni.var4astro.dao;

import cz.muni.var4astro.dto.PhotoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Objects;

/**
 * The Data Access Object implementation for the photo_properties entity.
 */
@Repository
@Transactional
public class PhotoPropertiesDaoImpl implements PhotoPropertiesDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void init(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void savePhotoProps(PhotoProperties photoProperties) {
        String insertQuery = "INSERT INTO photo_properties " +
                "(id, exposure_begin, exposure_end)" +
                "VALUES (nextval('photo_properties_id_seq'), ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{"id"});
            ps.setTimestamp(1, photoProperties.getExposureBegin());
            ps.setTimestamp(2, photoProperties.getExposureEnd());
            return ps;
        }, keyHolder);
        photoProperties.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

}
