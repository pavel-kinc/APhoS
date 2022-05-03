package cz.muni.var4astro.controllers;

import cz.muni.var4astro.dao.FluxDaoImpl;
import cz.muni.var4astro.dao.SpaceObjectDaoImpl;
import cz.muni.var4astro.dao.UserRepo;
import cz.muni.var4astro.dto.FluxUserTime;
import cz.muni.var4astro.dto.SpaceObject;
import cz.muni.var4astro.helper.Night;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import cz.muni.var4astro.utils.Conversions;
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
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
public class ObjectController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    SpaceObjectDaoImpl spaceObjectDao;

    @Autowired
    FluxDaoImpl fluxDao;

    @GetMapping("/object")
    public String displayObjectFluxes(@RequestParam(name = "ref-id") Long referenceObjectId,
                                      @RequestParam(name = "ref-cat-id") String refObjectCatalogId,
                                      @RequestParam(name = "id") Long id,
                                      @RequestParam(name = "catalog-id") String catalogId,
                                      @RequestParam(name = "apertures", required = false) String[] apertures,
                                      @RequestParam(name = "ref-apertures", required = false) String[] refApertures,
                                      @RequestParam(name = "show-saturated",
                                              defaultValue = "false") boolean showSaturated,
                                      Model model) {
        List<FluxUserTime> fluxes = fluxDao.getFluxesByObjId(id, referenceObjectId);
        List<String> users = fluxes
                .stream()
                .map(FluxUserTime::getUsername)
                .distinct()
                .collect(Collectors.toList());
        List<Night> nights = setMagnitudes(fluxes, apertures, refApertures);
        // if show-saturated is true we need all the fluxes for the table and
        // non-saturated fluxes for csv and graph so two lists of fluxes are needed
        List<FluxUserTime> fluxesToDisplayInTable = null;
        if (showSaturated) {
            fluxesToDisplayInTable = fluxes;
        }
        // filtering saturated values
        fluxes = fluxes
                .stream()
                .filter(flux -> !flux.getMagnitude().equals(Float.NEGATIVE_INFINITY))
                .collect(Collectors.toList());
        if (!showSaturated) {
            fluxesToDisplayInTable = fluxes;
        }
        model.addAttribute("nights", nights);
        model.addAttribute("users", users);
        model.addAttribute("fluxes", fluxes);
        model.addAttribute("fluxesForTable", fluxesToDisplayInTable);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("refCatalogId", refObjectCatalogId);
        return "object";
    }

    @PostMapping(value = "/object/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void generateCSV(@RequestParam(name = "object-id") Long objectId,
                            @RequestParam(name = "ref-object-id") Long referenceObjectId,
                            @RequestParam(name = "unwanted-users") String[] unwantedUsers,
                            @RequestParam(name = "apertures", required = false) String[] apertures,
                            @RequestParam(name = "ref-apertures", required = false) String[] refApertures,
                            @RequestParam(name = "add-data") Boolean addData,
                            @RequestParam(name = "text-file-format") Boolean textFileFormat,
                            HttpServletResponse response) throws IOException {
        List<String> unwantedUsersList = Arrays.asList(unwantedUsers);
        List<FluxUserTime> fluxes = fluxDao.getFluxesByObjId(objectId, referenceObjectId);
        setMagnitudes(fluxes, apertures, refApertures);
        fluxes = fluxes
                .stream()
                .filter(flux ->
                        !unwantedUsersList.contains(flux.getUsername())
                && !flux.getMagnitude().equals(Float.NEGATIVE_INFINITY))
                .collect(Collectors.toList());
        CsvMapper mapper = new CsvMapper();
        final CsvSchema schema = mapper.schemaFor(FluxUserTime.class)
                .withColumnSeparator(textFileFormat ? ' ' : ';');
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
                    + " " + spaceObject.getCatalogId()});
            seqWriter.write(new Object[]{"Catalog Right Ascension", spaceObject.getCatalogRec()});
            seqWriter.write(new Object[]{"Catalog Declination", spaceObject.getCatalogDec()});
            seqWriter.write(new Object[]{"Catalog Magnitude", spaceObject.getCatalogMag()});
            seqWriter.write(new Object[]{"Reference catalog ID", spaceObject.getCatalog()
                    + " " + refObject.getCatalogId()});
            seqWriter.write(new Object[]{"Reference catalog Right Ascension", refObject.getCatalogRec()});
            seqWriter.write(new Object[]{"Reference catalog Declination", refObject.getCatalogDec()});
            seqWriter.write(new Object[]{"Reference catalog Magnitude", refObject.getCatalogMag()});
        }
        seqWriter.write(new Object[]{"Exposure middle", "Magnitude", "Deviation"});
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
                Conversions.calculateMagnitudeAndDeviation(flux, nights);
        fluxes.forEach(consumer);
        return nights;
    }
}
