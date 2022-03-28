package com.example.astroapp.mappers;

import com.example.astroapp.dto.UploadLog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UploadLogMapper implements RowMapper<UploadLog> {

    @Override
    public UploadLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        UploadLog uploadLog = new UploadLog();
        uploadLog.setId(rs.getLong("id"));
        uploadLog.setUploadTime(rs.getTimestamp("time_of_upload"));
        uploadLog.setNumOfFiles(rs.getInt("success_cnt"));
        uploadLog.setNumOfErrors(rs.getInt("error_cnt"));
        return uploadLog;
    }
}
