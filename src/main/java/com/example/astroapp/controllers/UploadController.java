package com.example.astroapp.controllers;

import com.example.astroapp.exceptions.CsvContentException;
import com.example.astroapp.services.FileHandlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    FileHandlingService fileHandlingService;

    @GetMapping("")
    public String showAboutPage() {
        return "upload";
    }

    @PostMapping("/save")
    @ResponseBody
    public String storeUploadedFiles(@RequestParam(name = "file") MultipartFile file,
                                     @RequestParam String dirName)
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
    @ResponseBody
    public String parseAndSave(@RequestParam String pathToDir) throws FileNotFoundException {
        if (!Files.isDirectory(Paths.get(pathToDir))) {
            throw new FileNotFoundException("Given path to the directory is not correct.");
        }
        int unsuccessfulCount = 0;
        try {
            try (Stream<Path> filePaths = Files.walk(Paths.get(pathToDir))) {
                filePaths
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            try {
                                fileHandlingService.store(path);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }
        } catch (IOException | CsvContentException e) {
            e.printStackTrace();
            unsuccessfulCount++;
        }
        return Integer.toString(unsuccessfulCount);
    }
}