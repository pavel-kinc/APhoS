package cz.muni.aphos.dao;

import cz.muni.aphos.dto.UploadLog;
import cz.muni.aphos.dto.User;

import java.sql.Timestamp;
import java.util.List;


/**
 * The interface Upload logs dao, data access object for the uplaoding_logs entity.
 */
public interface UploadLogsDao {

    /**
     * Save the upload log to the database.
     *
     * @param uploadingUser the uploading user
     * @param uploadTime    the time of the upload
     * @param numOfFiles    the number of files
     * @param numOfErrors   the number of unsuccessfully uploaded files
     * @return the generated id
     */
    long saveUploadLog(User uploadingUser, Timestamp uploadTime, int numOfFiles,
                       int numOfErrors);

    /**
     * Gets logs for a user.
     *
     * @param userId the user id
     * @return the logs for user
     */
    List<UploadLog> getLogsForUser(String userId);
}
