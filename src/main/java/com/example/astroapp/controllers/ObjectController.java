package com.example.astroapp.controllers;

import com.example.astroapp.dao.FluxDao;
import com.example.astroapp.dao.SpaceObjectDao;
import com.example.astroapp.dao.UserRepo;
import com.example.astroapp.dto.FluxUserTime;
import com.example.astroapp.dto.GraphData;
import com.example.astroapp.entities.SpaceObject;
import com.example.astroapp.helper.Night;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.example.astroapp.utils.UnitConversions.convertFluxesToMagnitude;

@Controller
public class ObjectController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @Autowired
    FluxDao fluxDao;

    @GetMapping("/object")
    public String displayObjectFluxes(@RequestParam(name = "refId") Long referenceObjectId,
                                      @RequestParam(name = "refCatId") String refObjectCatalogId,
                                      @RequestParam(name = "id") Long id,
                                      @RequestParam(name = "catalogId") String catalogId,
                                      @RequestParam(name = "apertures", required = false) String[] apertures,
                                      @RequestParam(name = "refApertures", required = false) String[] refApertures,
                                      Model model) {
        List<FluxUserTime> fluxes = fluxDao.getFluxesByObjId(id, referenceObjectId);
        List<String> users = fluxes
                .stream()
                .map(FluxUserTime::getUsername)
                .distinct()
                .collect(Collectors.toList());
        List<Night> nights = setMagnitudes(fluxes, apertures, refApertures);
        Consumer<FluxUserTime> consumer = flux ->
                flux.setMagnitude(convertFluxesToMagnitude(flux, nights));
        fluxes.forEach(consumer);
        model.addAttribute("nights", nights);
        model.addAttribute("users", users);
        model.addAttribute("fluxes", fluxes);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("refCatalogId", refObjectCatalogId);
        return "object";
    }

    @PostMapping(value = "/object/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void generateCSV(@RequestParam Long objectId,
                            @RequestParam(name = "refObjectId") Long referenceObjectId,
                            @RequestParam String[] unwantedUsers,
                            @RequestParam(name = "apertures", required = false) String[] apertures,
                            @RequestParam(name = "refApertures", required = false) String[] refApertures,
                            @RequestParam Boolean addData,
                            HttpServletResponse response) throws IOException {
        List<String> unwantedUsersList = Arrays.asList(unwantedUsers);
        List<FluxUserTime> fluxes = fluxDao.getFluxesByObjId(objectId, referenceObjectId);
        setMagnitudes(fluxes, apertures, refApertures);
        fluxes = fluxes
                .stream()
                .filter(flux ->
                        !unwantedUsersList.contains(flux.getUsername()))
                .collect(Collectors.toList());
        CsvMapper mapper = new CsvMapper();
        final CsvSchema schema = mapper.schemaFor(FluxUserTime.class)
                .withColumnSeparator(';');
        File file = new File("file.csv");
        try (SequenceWriter seqWriter = mapper.writer(schema)
                .writeValues(file)) {
            writeInitialData(seqWriter, addData, spaceObjectDao.getSpaceObjectById(objectId),
                    spaceObjectDao.getSpaceObjectById(referenceObjectId));
            seqWriter.writeAll(fluxes);
        }
        try {
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream", ex);
        }
    }

    private void writeInitialData(SequenceWriter seqWriter, Boolean addData,
                                  SpaceObject spaceObject, SpaceObject refObject) throws IOException {
        if (addData) {
            seqWriter.write(new Object[]{"Catalog ID", spaceObject.getCatalog()
                    + " " + spaceObject.getCatalogID()});
            seqWriter.write(new Object[]{"Catalog Right Ascension", spaceObject.getCatalogRec()});
            seqWriter.write(new Object[]{"Catalog Declination", spaceObject.getCatalogDec()});
            seqWriter.write(new Object[]{"Catalog Magnitude", spaceObject.getCatalogMag()});
            seqWriter.write(new Object[]{"Reference catalog ID", spaceObject.getCatalog()
                    + " " + refObject.getCatalogID()});
            seqWriter.write(new Object[]{"Reference catalog Right Ascension", refObject.getCatalogRec()});
            seqWriter.write(new Object[]{"Reference catalog Declination", refObject.getCatalogDec()});
            seqWriter.write(new Object[]{"Reference catalog Magnitude", refObject.getCatalogMag()});
        }
        seqWriter.write(new Object[]{"RA", "Dec", "Magnitude", "Exposure begin", "Exposure end", "Uploaded by"});
    }

    private List<Night> setMagnitudes(List<FluxUserTime> fluxes, String[] apertures, String[] refApertures) {
        List<Night> nights = fluxes
                .stream()
                .map(FluxUserTime::getNight)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        for (int i = 0; i < nights.size(); i++) {
            Night night = nights.get(i);
            night.setIdOnPage(i);
            if (apertures != null && i < apertures.length && i < refApertures.length) {
                night.setApToBeUsed(apertures[i]);
                night.setRefApToBeUsed(refApertures[i]);
            } else {
                night.setApToBeUsed("auto");
                night.setRefApToBeUsed("auto");
            }
        }
        Consumer<FluxUserTime> consumer = flux ->
                flux.setMagnitude(convertFluxesToMagnitude(flux, nights));
        fluxes.forEach(consumer);
        return nights;
    }
}
