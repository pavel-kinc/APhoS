package com.example.astroapp.controllers;

import com.example.astroapp.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/upload")
public class UploadController {

//    private final StorageService storageService;
//
//    @Autowired
//    public UploadController(StorageService storageService) {
//        this.storageService = storageService;
//    }


    @GetMapping("")
    public String showAboutPage() {
        return "upload";
    }

    @PostMapping("/save")
    public String saveFiles(@RequestParam(name = "files") List<MultipartFile> files,
                            RedirectAttributes redirectAttributes) {
        StringBuilder fileNames = new StringBuilder();
        for (MultipartFile file : files) {
            fileNames.append(file.getOriginalFilename()).append("\n");
        }
        redirectAttributes.addFlashAttribute("message",
                "Files uploaded successfully: " + fileNames);
        return "redirect:/upload";
    }
}