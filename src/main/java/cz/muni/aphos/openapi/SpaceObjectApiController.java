package cz.muni.aphos.openapi;

import com.fasterxml.jackson.annotation.JsonView;
import cz.muni.aphos.dao.FluxDao;
import cz.muni.aphos.dao.SpaceObjectDao;
import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.dto.SpaceObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.aphos.dto.User;
import cz.muni.aphos.helper.ViewField;
import cz.muni.aphos.openapi.models.Catalog;
import cz.muni.aphos.openapi.models.ComparisonObject;
import cz.muni.aphos.openapi.models.Coordinates;
import cz.muni.aphos.openapi.models.SpaceObjectWithFluxes;
import cz.muni.aphos.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.xml.bind.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import cz.muni.aphos.services.FileHandlingService;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class SpaceObjectApiController implements SpaceObjectApi {

    private static final Logger log = LoggerFactory.getLogger(SpaceObjectApiController.class);

    @Autowired
    FileHandlingService fileHandlingService;


    @Autowired
    UserService userService;

    @Autowired
    private SpaceObjectDao spaceObjectDao;

    @Autowired
    private FluxDao fluxDao;


    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getLocalizedMessage() + " Illegal argument exception"), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<ObjectFluxCount>> findSpaceObjectsByParams(
            @Parameter(in = ParameterIn.QUERY, description = "Find object based on it's ID in given catalog") @Valid @RequestParam(value = "objectId", required = false) String objectId,
            @Parameter(in = ParameterIn.QUERY, description = "Catalog of space object to return \n\nDefault is " + Catalog.allValue) @Valid Catalog catalog,
            @Parameter(in = ParameterIn.QUERY, description = "Find object by it's name") @Valid @RequestParam(value = "name", required = false) String name,
            @Parameter(in = ParameterIn.QUERY, description = "Filter by coordinates\n\n" + "Format: " + Coordinates.example) @Nullable @Valid @RequestParam(value = "coordinates", required = false) String coordinates, @DecimalMin("0")
            @Parameter(in = ParameterIn.QUERY, description = "Find objects based on min magnitude" ,schema=@Schema(type="number", format="float", defaultValue="0")) @Valid @RequestParam(value = "minMag", required = false, defaultValue="0") Float minMag, @DecimalMax("20")
            @Parameter(in = ParameterIn.QUERY, description = "Find objects based on max magnitude" ,schema=@Schema(type="number", format="float", defaultValue="15")) @Valid @RequestParam(value = "maxMag", required = false, defaultValue="15") Float maxMag) {
        try{
            Coordinates coords;
            if(coordinates != null){
                ObjectMapper mapper = new ObjectMapper();
                coords= mapper.readValue(coordinates, Coordinates.class);
                if(!coords.isValid()){
                    throw new ValidationException("Coordinates value not correct, use: " + Coordinates.class.getAnnotation(Schema.class).example());
                }
            } else{
                coords = new Coordinates();
            }

            List<ObjectFluxCount> res = spaceObjectDao.queryObjects(coords.getRightAsc(), coords.getDeclination(),
                    coords.getRadius().toString(),
                    name != null ? name : "", minMag.toString(), maxMag.toString(),
                    catalog != null ? catalog.getValue() : "All catalogues", objectId != null ? objectId : "");
            return new ResponseEntity<>(res, HttpStatus.OK);

        } catch (Exception e){
            log.error("SpaceObject endpoint problem", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SpaceObject endpoint problem");
        }
    }


    @Override
    public ResponseEntity<SpaceObjectWithFluxes> getSpaceObjectById(
            @Parameter(in = ParameterIn.QUERY, description = "ID of space object to return", required=true) @Valid @RequestParam(value = "spaceObjectId") String spaceObjectId,
            @Parameter(in = ParameterIn.QUERY, description = "Catalog of space object to return \n\nDefault is " + Catalog.defaultValue)
            @Valid Catalog catalog) {
        try{
            SpaceObjectWithFluxes spaceObject = (SpaceObjectWithFluxes) spaceObjectDao.getSpaceObjectByObjectIdCat
                    (spaceObjectId, catalog != null ? catalog.getValue() : Catalog.defaultValue, true);
            if(spaceObject == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SpaceObject not found");
            }
            spaceObject.setFluxes(fluxDao.getFluxesByObj(spaceObject.getId()));
            spaceObject.setNumberOfFluxes(spaceObject.getFluxes().size());
            return new ResponseEntity<>(spaceObject, HttpStatus.OK);
        } catch(ResponseStatusException e){
            throw e;
        } catch(Exception e){
            log.error("SpaceObject endpoint problem", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SpaceObject internal server error");
        }
    }

    @Override
    public ResponseEntity<ComparisonObject> getComparisonByIdentificators(
            @Parameter(in = ParameterIn.QUERY, description = "ID of space object to return", required=true) @Valid @RequestParam() String originalId,
            @Parameter(in = ParameterIn.QUERY, description = "Catalog of space object to return") @Valid Catalog originalCat,
            @Parameter(in = ParameterIn.QUERY, description = "ID of space object to return", required=true) @Valid @RequestParam() String referenceId,
            @Parameter(in = ParameterIn.QUERY, description = "Catalog of space object to return") @Valid Catalog referenceCat){
        try{
            ObjectFluxCount original = spaceObjectDao.getSpaceObjectByObjectIdCat
                    (originalId, originalCat != null ? originalCat.getValue() : Catalog.defaultValue, false);
            if(original == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Original object not found");
            }
            ObjectFluxCount reference = spaceObjectDao.getSpaceObjectByObjectIdCat
                    (referenceId, referenceCat != null ? referenceCat.getValue() : Catalog.defaultValue, false);
            if(reference == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reference object not found");
            }
            List<FluxUserTime> data = fluxDao.getFluxesByObjId(original.getId(), reference.getId());
            original.setNumberOfFluxes(Math.toIntExact(spaceObjectDao.getSpaceObjectFluxCount(original.getId())));

            reference.setNumberOfFluxes(Math.toIntExact(spaceObjectDao.getSpaceObjectFluxCount(reference.getId())));

            return new ResponseEntity<>(new ComparisonObject(original, reference, data), HttpStatus.OK);
        }catch(ResponseStatusException e){
            throw e;
        }catch(Exception e){
            log.error("Comparison object endpoint error", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Comparison object endpoint error");
        }
    }

    @Override
    public ResponseEntity<String> uploadCSV (@RequestParam(required = true) MultipartFile file) throws IOException {
        Path path = null;
        if(file.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File not loaded");
        }
        try {
            Authentication auth = SecurityContextHolder.
                    getContext().getAuthentication();
            User user;
            if(auth != null && !auth.getPrincipal().toString().equals("anonymousUser")){
                user =userService.getCurrentUser();
            }else{
                user = new User("7899871233215");
                user.setUsername("Anonymous");
            }
            Path dirs = Paths.get("target/temp/parse/");
            Files.createDirectories(dirs);
            int append = 1;
            String filename = "file" + append + ".csv";
            String directory = dirs.toString();
            path = Paths.get(directory, filename);
            while(Files.exists(path)){
                append++;
                path = Paths.get(directory, "file" + append + ".csv");
            }
            //Files.createTempFile(String.valueOf(dirs),filename);
            file.transferTo(path);
            fileHandlingService.parseAndPersist(path, user);
            Files.deleteIfExists(path);
            return new ResponseEntity<>("File uploaded and data saved.", HttpStatus.OK);
        } catch (Exception e) {
            if(path != null){
                Files.deleteIfExists(path);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload file server problem");
        }

    }

}
