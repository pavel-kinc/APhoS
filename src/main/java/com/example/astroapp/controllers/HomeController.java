package com.example.astroapp.controllers;

import com.example.astroapp.dao.FluxDao;
import com.example.astroapp.dao.SpaceObjectDao;
import com.example.astroapp.dao.UserRepo;
import com.example.astroapp.dto.FluxUserTime;
import com.example.astroapp.dto.ObjectFlux;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @Autowired
    FluxDao fluxDao;

    @GetMapping("/")
    public String displayHome() {
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
                                 @RequestParam(name = "object_id") String objectId, Model model) throws SQLException {
        List<ObjectFlux> objectFluxList = spaceObjectDao.queryObjects(
                rightAscension, dec, radius, name, minMag, maxMag, catalog, objectId);
        model.addAttribute("resultingRows", objectFluxList);
        model.addAttribute("RA", rightAscension);
        model.addAttribute("dec", dec);
        model.addAttribute("radius", radius);
        model.addAttribute("name", name);
        model.addAttribute("minMag", minMag);
        model.addAttribute("maxMag", maxMag);
        model.addAttribute("catalog", catalog);
        model.addAttribute("objectId", objectId);
        return "home";
    }
}
