package cz.muni.var4astro.dao;

import cz.muni.var4astro.mappers.UploadMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * The Data Access Object implementation for the uploading_error_messages entity.
 */
@Repository
@Transactional
public class UploadErrorMessagesDaoImpl implements UploadErrorMessagesDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void init(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveUploadErrorMessage(long uploadingLogId, Pair<String, String> fileErrorMessagePair) {
        String insertQuery = "INSERT INTO uploading_error_messages " +
                "(id, uploading_log_id, filename, error_message)" +
                "VALUES (nextval('uploading_error_messages_id_seq'), ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{"id"});
            ps.setLong(1, uploadingLogId);
            ps.setString(2, fileErrorMessagePair.getFirst());
            ps.setString(3, fileErrorMessagePair.getSecond());
            return ps;
        }, keyHolder);
    }

    @Override
    public List<Pair<String, String>> getMessagesForLog(long logId) {
        String query = "SELECT * FROM uploading_error_messages WHERE uploading_log_id = ?";
        return jdbcTemplate.query(query, new UploadMessageMapper(), logId);
    }
}
