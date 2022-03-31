package com.example.astroapp.controllers;

import com.example.astroapp.dao.UploadErrorMessagesDao;
import com.example.astroapp.dao.UploadLogsDao;
import com.example.astroapp.dto.User;
import com.example.astroapp.exceptions.CsvContentException;
import com.example.astroapp.services.FileHandlingService;
import com.example.astroapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    FileHandlingService fileHandlingService;

    @Autowired
    UserService userService;

    @Autowired
    UploadLogsDao uploadLogsDao;

    @Autowired
    UploadErrorMessagesDao uploadErrorMessagesDao;

    @GetMapping("")
    public String showAboutPage() {
        return "upload";
    }

    @PostMapping("/save")
    @ResponseBody
    public String storeUploadedFiles(@RequestParam(name = "file") MultipartFile file,
                                     @RequestParam(name = "dir-name") String dirName)
            throws IOException {
        String path;
        if (dirName.equals("create_new")) {
            path = Files.createTempDirectory(
                    Path.of(System.getProperty("java.io.tmpdir")), "flux").toString();
        } else {
            path = dirName;
        }
        File normalFile = new File(path +
                "/" + file.getOriginalFilename());
        file.transferTo(normalFile);
        return path;
    }

    @PostMapping("/parse")
    @Transactional
    @ResponseBody
    public String parseAndSave(@RequestParam(name = "path-to-dir") String pathToDir,
                               @RequestParam(name = "file-count") int numOfFiles)
            throws FileNotFoundException {
        User uploadingUser = userService.getCurrentUser();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<Pair<String, String>> fileErrorMessagePairsList = new ArrayList<>();
        if (!Files.isDirectory(Paths.get(pathToDir))) {
            throw new FileNotFoundException("Given path to the directory is not correct.");
        }
        int unsuccessfulCount = 0;
        try {
            try (Stream<Path> filePaths = Files.walk(Paths.get(pathToDir))) {
                List<Path> regularFiles = filePaths
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());
                for (Path file : regularFiles) {
                    try {
                        fileHandlingService.store(file);
                    } catch (IOException | CsvContentException e) {
                        unsuccessfulCount++;
                        fileErrorMessagePairsList.add(Pair.of(
                                file.getFileName().toString(), e.getMessage()));
                    }
                }
            }
        } catch (IOException e) {
            logUploadData(uploadingUser, currentTime, numOfFiles, numOfFiles, new ArrayList<>());
            return "IOError";
        }
        logUploadData(uploadingUser, currentTime, numOfFiles,
                unsuccessfulCount, fileErrorMessagePairsList);
        return Integer.toString(unsuccessfulCount);
    }

    private void logUploadData(User uploadingUser, Timestamp uploadTime, int numOfFiles,
                               int numOfErrors, List<Pair<String, String>> fileErrorMessagePairsList) {
        long logId = uploadLogsDao.saveUploadLog(uploadingUser, uploadTime, numOfFiles, numOfErrors);
        for (Pair<String, String> errorMessagePair : fileErrorMessagePairsList) {
            uploadErrorMessagesDao.saveUploadErrorMessage(logId, errorMessagePair);
        }
    }
}