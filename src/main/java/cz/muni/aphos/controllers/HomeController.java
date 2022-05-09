package cz.muni.aphos.controllers;

import cz.muni.aphos.dao.FluxDao;
import cz.muni.aphos.dao.SpaceObjectDao;
import cz.muni.aphos.dto.ObjectFluxCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * The Home controller handles the home page and all the searching endpoints along with it.
 * The process of searching for the fluxes of an object is as follows:
 *   1. "/" endpoint: Search for a main object.
 *   2. "/search" endpoint: Choose a main object from the table of results.
 *   3. "/reference" endpoint: Search for a reference object.
 *   4. "/reference/search" endpoint: Choose a reference object from the table of results.
 *   5. "/object" endpoint: display the results (handled by ObjectController)
 */
@Controller
public class HomeController {


    @Autowired
    SpaceObjectDao spaceObjectDao;


    @Autowired
    FluxDao fluxDao;

    /**
     * Display the home page. A search form for the main object is available.
     *
     * @param model the model
     * @return the home.html template
     */
    @GetMapping("/")
    public String displayHome(Model model) {
        List<String> availableCatalogues = spaceObjectDao.getAvailableCatalogues();
        model.addAttribute("availableCatalogues", availableCatalogues);
        return "home";
    }

    /**
     * Display a table with space objects found matching the search form parameters.
     * User chooses a main space object from those.
     *
     * @param rightAscension the right ascension
     * @param dec            the declination
     * @param radius         the radius
     * @param name           the name
     * @param minMag         the lower magnitude boundary
     * @param maxMag         the upper magnitude boundary
     * @param catalog        the catalog
     * @param objectId       the catalog id
     * @param model          the model
     * @return the home.html template
     */
    @GetMapping("search")
    public String displayObjectResults(@RequestParam(name="right-ascension") String rightAscension,
                                       @RequestParam String dec,
                                       @RequestParam String radius,
                                       @RequestParam String name,
                                       @RequestParam(name = "min-mag") String minMag,
                                       @RequestParam(name = "max-mag") String maxMag,
                                       @RequestParam String catalog,
                                       @RequestParam(name = "object-id") String objectId, Model model) {
        addModelAttributes(rightAscension, dec, radius, name, minMag, maxMag,
                catalog, objectId, model);
        return "home";
    }

    /**
     * Display the search form for the reference object.
     *
     * @param originalId    the database id of the previously chosen main object
     * @param originalCatId the catalog id of the previously chosen main object
     * @param model         the model
     * @return the home.html template
     */
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

    /**
     * Display a table with space objects found matching the search form parameters.
     * User chooses a reference space object from those.
     *
     * @param originalId     the database id of the previously chosen main object
     * @param originalCatId  the catalog id of the previously chosen main object
     * @param rightAscension the right ascension
     * @param dec            the declination
     * @param radius         the radius
     * @param name           the name
     * @param minMag         the lower magnitude boundary
     * @param maxMag         the upper magnitude boundary
     * @param catalog        the catalog
     * @param objectId       the catalog id
     * @param model          the model
     * @return the string
     */
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
        model.addAttribute("originalId", originalId);
        model.addAttribute("originalCatId", originalCatId);
        addModelAttributes(rightAscension, dec, radius, name, minMag, maxMag, catalog,
                objectId, model);
        return "home";
    }

    /**
     * Block of code to avoid duplication. The search form parameters are added to model
     * after searching so the chosen values don't disappear from the search form.
     *
     * @param rightAscension the right ascension
     * @param dec declination
     * @param radius radius
     * @param name name
     * @param minMag the lower magnitude boundary
     * @param maxMag the upper magnitude boundary
     * @param catalog the catalog
     * @param objectId the catalog id
     * @param model the model
     */
    private void addModelAttributes(String rightAscension, String dec, String radius, String name, String minMag,
                                            String maxMag, String catalog, String objectId, Model model) {
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
    }
}
