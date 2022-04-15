package com.example.astroapp.mappers;

import com.example.astroapp.dto.UploadLog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


public class UploadLogMapper implements RowMapper<UploadLog> {

    @Override
    public UploadLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        UploadLog uploadLog = new UploadLog();
        uploadLog.setId(rs.getLong("id"));
        uploadLog.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                rs.getTimestamp("time_of_upload")));
        uploadLog.setNumOfFiles(rs.getInt("success_cnt"));
        uploadLog.setNumOfErrors(rs.getInt("error_cnt"));
        uploadLog.setNumOfSuccessful(uploadLog.getNumOfFiles() - uploadLog.getNumOfErrors());
        return uploadLog;
    }
}
