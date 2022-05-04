package cz.muni.var4astro.dao;

import cz.muni.var4astro.dto.UploadLog;
import cz.muni.var4astro.dto.User;

import java.sql.Timestamp;
import java.util.List;

public interface UploadLogsDao {

    long saveUploadLog(User uploadingUser, Timestamp uploadTime, int numOfFiles,
                       int numOfErrors);

    List<UploadLog> getLogsForUser(String userId);
}
