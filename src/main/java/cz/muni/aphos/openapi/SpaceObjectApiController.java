package cz.muni.aphos.openapi;

import cz.muni.aphos.dao.SpaceObjectDao;
import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.SpaceObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.aphos.openapi.models.Catalog;
import cz.muni.aphos.openapi.models.Coordinates;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-12-13T10:56:51.543Z[GMT]")
@RestController
public class SpaceObjectApiController implements SpaceObjectApi {

    private static final Logger log = LoggerFactory.getLogger(SpaceObjectApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private SpaceObjectDao spaceObjectDao;

    @org.springframework.beans.factory.annotation.Autowired
    public SpaceObjectApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<SpaceObject>>
    findSpaceObjectsByParams(@Parameter(in = ParameterIn.QUERY, description = "Find object based on it's ID in given catalog" , schema=@Schema()) @Valid @RequestParam(value = "objectId", required = false) String objectId,
                             @Parameter(in = ParameterIn.QUERY, description = "Find objects based on catalog" ,schema=@Schema()) @Valid @RequestParam(value = "catalog", required = false) Catalog catalog,
                             @Parameter(in = ParameterIn.QUERY, description = "Find object by it's name" , schema=@Schema()) @Valid @RequestParam(value = "name", required = false) String name,
                             @Parameter(in = ParameterIn.QUERY, description = "Filter by coordinates" , schema=@Schema()) @Valid @RequestParam(value = "coordinates", required = false) String coordinates,
                             @DecimalMin("0")@Parameter(in = ParameterIn.QUERY, description = "Find objects based on min magnitude" , schema=@Schema( defaultValue="0")) @Valid @RequestParam(value = "minMag", required = false, defaultValue="0") Float minMag,
                             @DecimalMax("15") @Parameter(in = ParameterIn.QUERY, description = "Find objects based on max magnitude" , schema=@Schema( defaultValue="15")) @Valid @RequestParam(value = "maxMag", required = false, defaultValue="15") Float maxMag) {
        try{
            Coordinates myCoordinates;
            String accept = request.getHeader("Accept");
            if(coordinates == null){
                myCoordinates = new Coordinates();
            } else{
                myCoordinates = objectMapper.readValue(coordinates, Coordinates.class);
            }
            List res = spaceObjectDao.queryObjects(myCoordinates.getRightAsc(), myCoordinates.getDeclination(),
                    myCoordinates.getRadius() != null ? myCoordinates.getRadius().toString() : "",
                    name != null ? name : "", minMag.toString(), maxMag.toString(),
                    catalog != null ? catalog.toString() : "All catalogues", objectId != null ? objectId : "");
            if (accept != null && accept.contains("application/json")) {
                String body = objectMapper.writeValueAsString(res);
                try {
                    return new ResponseEntity<List<SpaceObject>>(objectMapper.readValue(body, List.class), HttpStatus.OK);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<List<SpaceObject>>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return new ResponseEntity<List<SpaceObject>>(res, HttpStatus.OK);
        } catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<List<SpaceObject>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FluxUserTime> getSpaceObjectById(@Parameter(in = ParameterIn.PATH, description = "Catalog of space object to return", required=true, schema=@Schema()) @PathVariable("catalog") Catalog catalog, @Parameter(in = ParameterIn.PATH, description = "ID of space object to return", required=true, schema=@Schema()) @PathVariable("spaceObjectId") String spaceObjectId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<FluxUserTime>(objectMapper.readValue("{\n  \"fluxes\" : [ {\n    \"expMiddle\" : \"expMiddle\",\n    \"apAuto\" : 1.4658129805029452,\n    \"rightAsc\" : \"rightAsc\",\n    \"declination\" : \"declination\",\n    \"addedBy\" : \"addedBy\",\n    \"photo\" : {\n      \"exposureBegin\" : \"2021-09-09 14:15:30.9905\",\n      \"exposureEnd\" : \"2021-09-09 14:27:59.554\"\n    },\n    \"magnitude\" : 0.8008282,\n    \"deviation\" : 6.0274563,\n    \"apertures\" : [ 5.962133916683182, 5.962133916683182 ],\n    \"apAutoCmp\" : 5.637376656633329,\n    \"aperturesCmp\" : [ 2.3021358869347655, 2.3021358869347655 ]\n  }, {\n    \"expMiddle\" : \"expMiddle\",\n    \"apAuto\" : 1.4658129805029452,\n    \"rightAsc\" : \"rightAsc\",\n    \"declination\" : \"declination\",\n    \"addedBy\" : \"addedBy\",\n    \"photo\" : {\n      \"exposureBegin\" : \"2021-09-09 14:15:30.9905\",\n      \"exposureEnd\" : \"2021-09-09 14:27:59.554\"\n    },\n    \"magnitude\" : 0.8008282,\n    \"deviation\" : 6.0274563,\n    \"apertures\" : [ 5.962133916683182, 5.962133916683182 ],\n    \"apAutoCmp\" : 5.637376656633329,\n    \"aperturesCmp\" : [ 2.3021358869347655, 2.3021358869347655 ]\n  } ]\n}", FluxUserTime.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<FluxUserTime>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<FluxUserTime>(HttpStatus.NOT_IMPLEMENTED);
    }


}
