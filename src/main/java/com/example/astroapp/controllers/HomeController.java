package com.example.astroapp.controllers;

import com.example.astroapp.dao.SpaceObjectDao;
import com.example.astroapp.dao.UserRepo;
import com.example.astroapp.dto.ObjectFlux;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @GetMapping("/")
    public String displayHome(Model model) {
        model.addAttribute("users", userRepo.findAllUsersWhoHaveUploaded());
        return "home";
    }

    @GetMapping("search")
    public String displayResults(@RequestParam String rightAscension,
                                 @RequestParam String dec,
                                 @RequestParam String radius,
                                 @RequestParam String name,
                                 @RequestParam(name = "min_mag") String minMag,
                                 @RequestParam(name = "max_mag") String maxMag,
                                 @RequestParam String catalog,
                                 @RequestParam(name = "object_id") String objectId, RedirectAttributes redirectAttributes) throws SQLException {
        redirectAttributes.addFlashAttribute("users", userRepo.findAllUsersWhoHaveUploaded());
        List<ObjectFlux> objectFluxList = spaceObjectDao.queryObjects(
                rightAscension, dec, radius, name, minMag, maxMag, catalog, objectId);
        redirectAttributes.addFlashAttribute("resultingRows", objectFluxList);
        return "redirect:/search";
    }
}
