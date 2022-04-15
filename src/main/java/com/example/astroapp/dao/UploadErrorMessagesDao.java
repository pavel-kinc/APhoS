package com.example.astroapp.dao;

import com.example.astroapp.mappers.UploadMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * The Data Access Object for the uploading_error_messages entity.
 */
@Repository
@Transactional
public class UploadErrorMessagesDao extends JdbcDaoSupport {

    @Autowired
    UploadErrorMessagesDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public void saveUploadErrorMessage(long uploadingLogId, Pair<String, String> fileErrorMessagePair) {
        assert getJdbcTemplate() != null;
        String insertQuery = "INSERT INTO uploading_error_messages " +
                "(id, uploading_log_id, filename, error_message)" +
                "VALUES (nextval('uploading_error_messages_id_seq'), ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{"id"});
            ps.setLong(1, uploadingLogId);
            ps.setString(2, fileErrorMessagePair.getFirst());
            ps.setString(3, fileErrorMessagePair.getSecond());
            return ps;
        }, keyHolder);
    }

    public List<Pair<String, String>> getMessagesForLog(long logId) {
        assert getJdbcTemplate() != null;
        String query = "SELECT * FROM uploading_error_messages WHERE uploading_log_id = ?";
        return getJdbcTemplate().query(query, new UploadMessageMapper(), logId);
    }
}
