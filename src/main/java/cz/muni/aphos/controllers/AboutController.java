package cz.muni.aphos.controllers;

import cz.muni.aphos.AphosApplication;
import cz.muni.aphos.dao.FluxDao;
import cz.muni.aphos.dao.SpaceObjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The controller handles view of the "about" page of the website.
 */
@Controller
public class AboutController {

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @Autowired
    FluxDao fluxDao;

    /**
     * Handles requests to the /about endpoint and displays the about page.
     *
     * @param model the model
     * @return the about.html template
     */
    @GetMapping("/about")
    public String showAboutPage(Model model) {
        String fluxNumberFormatted = String.format("%,d", fluxDao.getNumberOfFluxesEstimate());
        String objectNumberFormatted = String.format("%,d", spaceObjectDao.getNumberOfObjectsEstimate());
        model.addAttribute("fluxNum", fluxNumberFormatted);
        model.addAttribute("objectNum", objectNumberFormatted);
        model.addAttribute("version", AphosApplication.class.getPackage().getImplementationVersion());
        return "about";
    }
}
