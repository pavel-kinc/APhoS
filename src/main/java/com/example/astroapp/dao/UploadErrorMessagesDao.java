package com.example.astroapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

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
}
