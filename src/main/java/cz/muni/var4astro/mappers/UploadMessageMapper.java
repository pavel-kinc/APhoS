package cz.muni.var4astro.mappers;

import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Upload message mapper maps one row from the uploading_error_messages table to a
 * Pair of Strings: (filename, error in the file).
 */
public class UploadMessageMapper implements RowMapper<Pair<String, String>> {

    @Override
    public Pair<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
        String filename = rs.getString("filename");
        String errorMessage = rs.getString("error_message");
        return Pair.of(filename, errorMessage);
    }
}
