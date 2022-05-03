package cz.muni.var4astro.dao;

import org.springframework.data.util.Pair;

import java.util.List;

public interface UploadErrorMessagesDao {

    void saveUploadErrorMessage(long uploadingLogId, Pair<String, String> fileErrorMessagePair);

    List<Pair<String, String>> getMessagesForLog(long logId);
}
