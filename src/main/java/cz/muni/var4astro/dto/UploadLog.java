package cz.muni.var4astro.dto;

import org.springframework.data.util.Pair;

import java.sql.Timestamp;
import java.util.List;

/**
 * The Data Transfer Object representing the information about an upload.
 * With the purpose of being later viewed by the user.
 */
public class UploadLog {

    private long id;

    /**
     * The time of the upload
     */
    private String uploadTime;
    /**
     * Number of files in the set of uploaded files
     */
    private int numOfFiles;
    /**
     * Number of files that were not successfully stored or uploaded
     */
    private int numOfErrors;
    /**
     * Number of files were successfully stored and uploaded
     */
    private int numOfSuccessful;
    /**
     * The error messages are stored in list of pairs: (filename, error in that file).
     */
    private List<Pair<String, String>> fileErrorMessagePairsList;

    public UploadLog() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public int getNumOfFiles() {
        return numOfFiles;
    }

    public void setNumOfFiles(int numOfFiles) {
        this.numOfFiles = numOfFiles;
    }

    public int getNumOfErrors() {
        return numOfErrors;
    }

    public void setNumOfErrors(int numOfErrors) {
        this.numOfErrors = numOfErrors;
    }

    public int getNumOfSuccessful() {
        return numOfSuccessful;
    }

    public void setNumOfSuccessful(int numOfSuccessful) {
        this.numOfSuccessful = numOfSuccessful;
    }

    public List<Pair<String, String>> getFileErrorMessagePairsList() {
        return fileErrorMessagePairsList;
    }

    public void setFileErrorMessagePairsList(List<Pair<String, String>> fileErrorMessagePairsList) {
        this.fileErrorMessagePairsList = fileErrorMessagePairsList;
    }
}
