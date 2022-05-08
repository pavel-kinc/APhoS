package cz.muni.var4astro.dao;

import cz.muni.var4astro.dto.UploadLog;
import cz.muni.var4astro.dto.User;
import cz.muni.var4astro.mappers.UploadLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * The Data Access Object implementation for the uploading_logs entity.
 */
@Repository
@Transactional
public class UploadLogsDaoImpl implements UploadLogsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void init(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public long saveUploadLog(User uploadingUser, Timestamp uploadTime, int numOfFiles,
                              int numOfErrors) {
        assert jdbcTemplate != null;
        String insertQuery = "INSERT INTO uploading_logs " +
                "(id, user_id, time_of_upload, success_cnt, error_cnt)" +
                "VALUES (nextval('uploading_logs_id_seq'), ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{"id"});
            ps.setString(1, uploadingUser.getGoogleSub());
            ps.setTimestamp(2, uploadTime);
            ps.setInt(3, numOfFiles);
            ps.setInt(4, numOfErrors);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<UploadLog> getLogsForUser(String userId) {
        assert jdbcTemplate != null;
        String query = "SELECT * FROM uploading_logs WHERE user_id = ?";
        return jdbcTemplate.query(query, new UploadLogMapper(), userId);
    }
}
