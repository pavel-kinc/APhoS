package com.example.astroapp.dao;

import com.example.astroapp.dto.UploadLog;
import com.example.astroapp.dto.User;
import com.example.astroapp.mappers.UploadLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * The Data Access Object for the uploading_logs entity.
 */
@Repository
@Transactional
public class UploadLogsDao extends JdbcDaoSupport {

    @Autowired
    UploadLogsDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public long saveUploadLog(User uploadingUser, Timestamp uploadTime, int numOfFiles,
                              int numOfErrors) {
        assert getJdbcTemplate() != null;
        String insertQuery = "INSERT INTO uploading_logs " +
                "(id, user_id, time_of_upload, success_cnt, error_cnt)" +
                "VALUES (nextval('uploading_logs_id_seq'), ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{"id"});
            ps.setString(1, uploadingUser.getGoogleSub());
            ps.setTimestamp(2, uploadTime);
            ps.setInt(3, numOfFiles);
            ps.setInt(4, numOfErrors);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<UploadLog> getLogsForUser(String userId) {
        assert getJdbcTemplate() != null;
        String query = "SELECT * FROM uploading_logs WHERE user_id = ?";
        return getJdbcTemplate().query(query, new UploadLogMapper(), userId);
    }
}
