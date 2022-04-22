package com.example.astroapp.controllers;

import com.example.astroapp.dao.FluxDao;
import com.example.astroapp.dao.SpaceObjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @Autowired
    FluxDao fluxDao;

    @GetMapping("/about")
    public String showAboutPage(Model model) {
        String fluxNumberFormatted = String.format("%,d", fluxDao.getNumberOfFluxesEstimate());
        String objectNumberFormatted = String.format("%,d", spaceObjectDao.getNumberOfObjectsEstimate());
        model.addAttribute("fluxNum", fluxNumberFormatted);
        model.addAttribute("objectNum", objectNumberFormatted);
        return "about";
    }
}
