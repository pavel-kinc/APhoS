package cz.muni.var4astro.controllers;

import cz.muni.var4astro.dao.UploadErrorMessagesDao;
import cz.muni.var4astro.dao.UploadErrorMessagesDaoImpl;
import cz.muni.var4astro.dao.UploadLogsDao;
import cz.muni.var4astro.dao.UploadLogsDaoImpl;
import cz.muni.var4astro.dto.User;
import cz.muni.var4astro.exceptions.CsvContentException;
import cz.muni.var4astro.services.FileHandlingService;
import cz.muni.var4astro.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * The Upload controller handles the upload of the files.
 */
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

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    /**
     * The Timeout for the SSEmitter.
     */
    static final long TIMEOUT_INFINITY = -1L;

    /**
     * Show the upload form.
     *
     * @return the upload.html template
     */
    @GetMapping("")
    public String showUploadPage() {
        return "upload";
    }

    /**
     * This endpoint receives a file form the client-side and
     * saves it in the temporary directory. A new subfolder is created
     * within the /tmp directory with the first file uploaded. Subsequent files
     * from the same set of uploaded files are then stored in this folder.
     *
     * @param file    the multipart file
     * @param dirName the name of the directory to save the file in
     *                "create_new" in case of first file from the set of files
     * @return the path to the saved file
     * @throws IOException in case of an IO error
     */
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

    /**
     * Parse and persist the files uploaded to the /tmp directory.
     * The method uses an SsseEmitter to send the information about progress
     * to the cliend-side which uses it to update the progress bar. It does
     * so after each file is handled.
     *
     * @param pathToDir  the path to directory with the files
     * @param numOfFiles the number of files
     * @return the sse emitter object which will send updates to the client-side
     */
    @GetMapping("/parse")
    public SseEmitter parseAndPersist(@RequestParam(name = "path-to-dir") String pathToDir,
                                      @RequestParam(name = "file-count") int numOfFiles) {
        // need to get user beforehand because the security context is lost in a new thread
        User uploadingUser = userService.getCurrentUser();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<Pair<String, String>> fileErrorMessagePairsList = new ArrayList<>();
        AtomicInteger unsuccessfulCount = new AtomicInteger();
        SseEmitter emitter = new SseEmitter(TIMEOUT_INFINITY);
        ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
        // separate thread because of SseEmitter
        sseExecutor.execute(() -> {
            try {
                if (!Files.isDirectory(Paths.get(pathToDir))) {
                    throw new FileNotFoundException("Given path to the directory is not correct.");
                }
//                 Prevent getting path outside /tmp, comment in case of testing
                if (!pathToDir.matches("/tmp/[^/]*")) {
                    throw new FileNotFoundException("Given path to the directory is not correct.");
                }
                try (Stream<Path> filePaths = Files.walk(Paths.get(pathToDir))) {
                    List<Path> regularFiles = filePaths
                            .filter(Files::isRegularFile)
                            .collect(Collectors.toList());
                    for (Path file : regularFiles) {
                        try {
                            fileHandlingService.parseAndPersist(file, uploadingUser);
                            log.info(file.getFileName() + " successfully parsed and stored");
                            emitter.send(SseEmitter.event()
                                    .name("FILE_STORED")
                                    .data(file.getFileName()));
                        } catch (IOException | CsvContentException e) {
                            log.error(file.getFileName() + " could not be parsed", e);
                            unsuccessfulCount.getAndIncrement();
                            fileErrorMessagePairsList.add(Pair.of(
                                    file.getFileName().toString(), e.getMessage()));
                        }
                    }
                    emitter.send(SseEmitter.event()
                            .name("COMPLETED")
                            .data(unsuccessfulCount));
                    emitter.complete();
                } catch (IOException e) {
                    logUploadData(uploadingUser, currentTime, numOfFiles, numOfFiles,
                            new ArrayList<>());
                    emitter.completeWithError(e);
                }
                logUploadData(uploadingUser, currentTime, numOfFiles,
                        unsuccessfulCount.get(), fileErrorMessagePairsList);
            } catch (Exception e) {
                log.error("emitter completed with error", e);
                emitter.completeWithError(e);
            }
        });
        sseExecutor.shutdown();
        return emitter;
    }


    /**
     * Save information about upload results to the database for user to later see.
     *
     * @param uploadingUser uploading sser
     * @param uploadTime the time of the upload
     * @param numOfFiles number of files
     * @param numOfErrors number of errors
     * @param fileErrorMessagePairsList List of pairs: (filename, error message for that file)
     */
    private void logUploadData(User uploadingUser, Timestamp uploadTime, int numOfFiles,
                               int numOfErrors, List<Pair<String, String>> fileErrorMessagePairsList) {
        long logId = uploadLogsDao.saveUploadLog(uploadingUser, uploadTime, numOfFiles, numOfErrors);
        for (Pair<String, String> errorMessagePair : fileErrorMessagePairsList) {
            uploadErrorMessagesDao.saveUploadErrorMessage(logId, errorMessagePair);
        }
    }
}