package com.example.astroapp.controllers;

import com.example.astroapp.dao.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/")
    public String displayHome(Model model) {
        model.addAttribute("users", userRepo.findAllUsersWhoHaveUploaded());
        return "home";
    }
}
