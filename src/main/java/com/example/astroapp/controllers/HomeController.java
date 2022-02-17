package com.example.astroapp.controllers;

import com.example.astroapp.dao.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/")
    public String displayHome(Model model) {
        model.addAttribute("users", userRepo.findAllUsersWhoHaveUploaded());
        return "home";
    }

    @GetMapping("search")
    public String displayResults(@RequestParam String coordinates,
                                 @RequestParam String radius,
                                 @RequestParam String name,
                                 @RequestParam(name = "min_mag") String minMag,
                                 @RequestParam(name = "max_mag") String maxMag,
                                 @RequestParam String catalog,
                                 @RequestParam(name = "object_id") String objectId, Model model) {
        model.addAttribute("users", userRepo.findAllUsersWhoHaveUploaded());

        return "home";
    }
}
