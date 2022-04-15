package com.example.astroapp.dto;

import org.springframework.data.util.Pair;

import java.sql.Timestamp;
import java.util.List;

/**
 * The Data Transfer Object for the upload log.
 * The error messages are stored in list of pairs of filename and error in that file.
 */
public class UploadLog {

    private long id;
    private String uploadTime;
    private int numOfFiles;
    private int numOfErrors;
    private int numOfSuccessful;
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
