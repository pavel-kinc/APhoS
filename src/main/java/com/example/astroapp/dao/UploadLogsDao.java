package com.example.astroapp.dao;

import com.example.astroapp.dto.UploadLog;
import com.example.astroapp.dto.User;

import java.sql.Timestamp;
import java.util.List;

public interface UploadLogsDao {

    long saveUploadLog(User uploadingUser, Timestamp uploadTime, int numOfFiles,
                       int numOfErrors);

    List<UploadLog> getLogsForUser(String userId);
}
