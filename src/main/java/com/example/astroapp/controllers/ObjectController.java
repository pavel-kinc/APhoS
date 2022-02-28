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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    public void generateCSV(@RequestParam(required = false) String objectID,
                                     @RequestParam(required = false) String[] unwantedUsers, HttpServletResponse response) {
        try {
            File file = new File("nove.csv");
            try (FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write("Sample file");
            }
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
            response.flushBuffer();
            int a = 1;
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream", ex);
        }
    }
}
