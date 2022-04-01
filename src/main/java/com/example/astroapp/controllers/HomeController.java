package com.example.astroapp.controllers;

import com.example.astroapp.dao.FluxDao;
import com.example.astroapp.dao.SpaceObjectDao;
import com.example.astroapp.dto.ObjectFluxCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @Autowired
    FluxDao fluxDao;

    @GetMapping("/")
    public String displayHome(Model model) {
        List<String> availableCatalogues = spaceObjectDao.getAvailableCatalogues();
        model.addAttribute("availableCatalogues", availableCatalogues);
        return "home";
    }

    @GetMapping("search")
    public String displayResults(@RequestParam(name="right-ascension") String rightAscension,
                                 @RequestParam String dec,
                                 @RequestParam String radius,
                                 @RequestParam String name,
                                 @RequestParam(name = "min-mag") String minMag,
                                 @RequestParam(name = "max-mag") String maxMag,
                                 @RequestParam String catalog,
                                 @RequestParam(name = "object-id") String objectId, Model model) {
        List<ObjectFluxCount> objectFluxCountList = spaceObjectDao.queryObjects(
                rightAscension, dec, radius, name, minMag, maxMag, catalog, objectId);
        List<String> availableCatalogues = spaceObjectDao.getAvailableCatalogues();
        model.addAttribute("availableCatalogues", availableCatalogues);
        model.addAttribute("resultingRows", objectFluxCountList);
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

    @GetMapping("reference")
    public String displayReferenceObjectFinder(@RequestParam(name = "id") Long originalId,
                                               @RequestParam(name = "cat-id") String originalCatId,
                                               Model model) {
        List<String> availableCatalogues = spaceObjectDao.getAvailableCatalogues();
        model.addAttribute("availableCatalogues", availableCatalogues);
        model.addAttribute("originalId", originalId);
        model.addAttribute("originalCatId", originalCatId);
        return "home";
    }

    @GetMapping("reference/search")
    public String displayReferenceObjectResults(
            @RequestParam(name = "id") Long originalId,
            @RequestParam(name = "cat-id") String originalCatId,
            @RequestParam(name = "right-ascension") String rightAscension,
            @RequestParam String dec,
            @RequestParam String radius,
            @RequestParam String name,
            @RequestParam(name = "min-mag") String minMag,
            @RequestParam(name = "max-mag") String maxMag,
            @RequestParam String catalog,
            @RequestParam(name = "object-id") String objectId, Model model) {
        List<ObjectFluxCount> objectFluxCountList = spaceObjectDao.queryObjects(
                rightAscension, dec, radius, name, minMag, maxMag, catalog, objectId);
        List<String> availableCatalogues = spaceObjectDao.getAvailableCatalogues();
        model.addAttribute("originalId", originalId);
        model.addAttribute("originalCatId", originalCatId);
        model.addAttribute("availableCatalogues", availableCatalogues);
        model.addAttribute("resultingRows", objectFluxCountList);
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
