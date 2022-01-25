package com.example.astroapp.controllers;

import com.example.astroapp.services.FileHandlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


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
//        fileHandlingService.store(file);
        return path;
    }
}