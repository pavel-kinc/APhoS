package com.example.astroapp.controllers;

import com.example.astroapp.dao.UserRepo;
import com.example.astroapp.entities.User;
import com.example.astroapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String displayProfile(@RequestParam String id, Model model) {
        User user = userRepo.findByUserID(id);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/username")
    public String displayUsernameForm() {
        return "usernameForm";
    }

    @RequestMapping("/username/save")
    public String saveUsername(@RequestParam String username) {
        User user = userService.getCurrentUser();
        user.setUsername(username);
        userRepo.save(user);
        return "redirect:/profile/?id="+user.getGoogleSub();
    }

}
