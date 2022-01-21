package com.example.astroapp.controllers;

import com.example.astroapp.services.FileHandlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


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
    public String handleFileUpload(@RequestParam(name = "file") MultipartFile file)
            throws IOException {
        fileHandlingService.store(file);
        return "redirect:/upload";
    }
}