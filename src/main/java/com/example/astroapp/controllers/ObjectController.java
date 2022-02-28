package com.example.astroapp.controllers;

import com.example.astroapp.dao.FluxDao;
import com.example.astroapp.dao.UserRepo;
import com.example.astroapp.dto.FluxUserTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.example.astroapp.utils.UnitConversions.convertFluxesToMagnitude;

@Controller
public class ObjectController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    FluxDao fluxDao;

    @GetMapping("/object")
    public String displayObjectFluxes(@RequestParam(name = "refId") Long referenceObjectId,
                                      @RequestParam(name = "refCatId") String refObjectCatalogId,
                                      @RequestParam(name = "id") Long id,
                                      @RequestParam(name = "catalogId") String catalogId,
                                      Model model) {
        List<FluxUserTime> fluxes = fluxDao.getFluxesByObjId(id, referenceObjectId);
        List<String> users = fluxes
                .stream()
                .map(FluxUserTime::getUsername)
                .distinct()
                .collect(Collectors.toList());
        Consumer<FluxUserTime> consumer = flux ->
                flux.setMagnitude(convertFluxesToMagnitude(flux.getApAuto(), flux.getRefApAuto()));
        fluxes.forEach(consumer);
        model.addAttribute("users", users);
        model.addAttribute("fluxes", fluxes);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("refCatalogId", refObjectCatalogId);
        return "object";
    }

    @PostMapping(value = "/object/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void generateCSV(@RequestParam String objectID,
                                          @RequestParam String[] unwantedUsers, HttpServletResponse response) {
        try {
            InputStream is = new FileInputStream("nove");
            FileCopyUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
