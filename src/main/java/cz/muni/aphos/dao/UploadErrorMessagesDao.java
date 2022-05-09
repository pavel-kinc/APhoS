package cz.muni.aphos.dao;

import org.springframework.data.util.Pair;

import java.util.List;

/**
 * The interface Upload error messages dao, data access object for the
 * uploading_error_messages entity.
 */
public interface UploadErrorMessagesDao {

    /**
     * Save upload error message.
     *
     * @param uploadingLogId       the id of the corresponding uploading log
     * @param fileErrorMessagePair the pair (filename, error message)
     */
    void saveUploadErrorMessage(long uploadingLogId, Pair<String, String> fileErrorMessagePair);

    /**
     * Gets error messages for a given log.
     *
     * @param logId the log id
     * @return the messages for log
     */
    List<Pair<String, String>> getMessagesForLog(long logId);
}
