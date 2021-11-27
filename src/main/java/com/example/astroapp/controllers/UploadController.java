package com.example.astroapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

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
    public String saveFiles(@RequestParam(name = "file") MultipartFile file,
                            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message",
                file.getOriginalFilename() + " uploaded successfully");
        return "redirect:/upload";
    }
}