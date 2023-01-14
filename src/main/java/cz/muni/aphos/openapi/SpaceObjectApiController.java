package cz.muni.aphos.openapi;

import cz.muni.aphos.dao.FluxDao;
import cz.muni.aphos.dao.SpaceObjectDao;
import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.dto.SpaceObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.aphos.openapi.models.Catalog;
import cz.muni.aphos.openapi.models.Coordinates;
import cz.muni.aphos.openapi.models.SpaceObjectWithFluxes;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SpaceObjectApiController implements SpaceObjectApi {

    private static final Logger log = LoggerFactory.getLogger(SpaceObjectApiController.class);

    private final HttpServletRequest request;

    @Autowired
    private SpaceObjectDao spaceObjectDao;

    @Autowired
    private FluxDao fluxDao;

    @Autowired
    public SpaceObjectApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.request = request;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<ObjectFluxCount>> findSpaceObjectsByParams(
            @Parameter(in = ParameterIn.QUERY, description = "Find object based on it's ID in given catalog") @Valid @RequestParam(value = "objectId", required = false) String objectId,
            @Parameter(in = ParameterIn.QUERY, description = "Find objects based on catalog", schema=@Schema(allowableValues={ "UCAC4", "USNO-B1.0" })) @Valid @RequestParam(value = "catalog", required = false) String catalog,
            @Parameter(in = ParameterIn.QUERY, description = "Find object by it's name") @Valid @RequestParam(value = "name", required = false) String name,
            @Parameter(in = ParameterIn.QUERY, description = "Filter by coordinates", example = "{\"rightAsc\":\"21:41:55.291\",\"declination\":\"71:18:41.12\",\"radius\":0.05}") @Nullable @Valid Coordinates coordinates, @DecimalMin("0")
            @Parameter(in = ParameterIn.QUERY, description = "Find objects based on min magnitude" ,schema=@Schema( defaultValue="0")) @Valid @RequestParam(value = "minMag", required = false, defaultValue="0") Float minMag, @DecimalMax("15")
            @Parameter(in = ParameterIn.QUERY, description = "Find objects based on max magnitude" ,schema=@Schema( defaultValue="15")) @Valid @RequestParam(value = "maxMag", required = false, defaultValue="15") Float maxMag) {
        try{
            List<ObjectFluxCount> res = spaceObjectDao.queryObjects(coordinates.getRightAsc(), coordinates.getDeclination(),
                    coordinates.getRadius().toString(),
                    name != null ? name : "", minMag.toString(), maxMag.toString(),
                    catalog != null ? catalog.toString() : "All catalogues", objectId != null ? objectId : "");

            return new ResponseEntity<>(res, HttpStatus.OK);

        } catch (Exception e){
            log.error("SpaceObject endpoint problem", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<SpaceObjectWithFluxes> getSpaceObjectById(
            @Parameter(in = ParameterIn.QUERY, description = "ID of space object to return", required=true) @Valid @RequestParam(value = "spaceObjectId") String spaceObjectId,
            @Parameter(in = ParameterIn.QUERY, description = "Catalog of space object to return", schema=@Schema(allowableValues={ "UCAC4", "USNO-B1.0" }))
            @Valid @RequestParam(value = "catalog", defaultValue = "UCAC4") String catalog) {
        SpaceObjectWithFluxes spaceObject = spaceObjectDao.getSpaceObjectByObjectIdCat(spaceObjectId, catalog!=null ? catalog.toString() : "");

        //spaceObject.setFluxes(fluxDao.getFluxesByObj(spaceObject.getId()));

        return new ResponseEntity<>(spaceObject, HttpStatus.OK);
    }


}
