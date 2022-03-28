package com.example.astroapp.mappers;

import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UploadMessageMapper implements RowMapper<Pair<String, String>> {

    @Override
    public Pair<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
        String filename = rs.getString("filename");
        String errorMessage = rs.getString("error_message");
        return Pair.of(filename, errorMessage);
    }
}
