package com.example.astroapp.controllers;

import com.example.astroapp.dao.FluxDao;
import com.example.astroapp.dao.UserRepo;
import com.example.astroapp.dto.FluxUserTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ObjectController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    FluxDao fluxDao;

    @GetMapping("/object")
    public String displayObjectFluxes(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "catId") String catalogId, Model model) {
        List<FluxUserTime> fluxes = fluxDao.getFluxesByObjId(id);
        List<String> users = fluxes
                .stream()
                .map(FluxUserTime::getUsername)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("fluxes", fluxes);
        model.addAttribute("catalogId", catalogId);
        return "object";
    }
}
