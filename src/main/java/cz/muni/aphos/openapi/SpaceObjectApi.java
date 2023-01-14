package cz.muni.aphos.openapi;

import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.openapi.models.Catalog;
import cz.muni.aphos.openapi.models.Coordinates;
import cz.muni.aphos.dto.SpaceObject;
//import io.swagger.model.SpaceObjectWithFluxes;
import cz.muni.aphos.openapi.models.Flux;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;


import java.util.List;
import java.util.Map;

@Validated
public interface SpaceObjectApi {

    @Operation(summary = "Finds space objects by multiple data", description = "No additional data is mandatory, but maximum object count is 100", tags={ "SpaceObject" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation",
                        content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = SpaceObject.class)))),

        @ApiResponse(responseCode = "400", description = "Invalid values") })
    @RequestMapping(value = "api/spaceObject/findByParams",
        produces = { "application/json", "application/xml" },
        method = RequestMethod.GET)
    ResponseEntity<List<ObjectFluxCount>> findSpaceObjectsByParams(@Parameter(in = ParameterIn.QUERY, description = "Find object based on it's ID in given catalog") @Valid @RequestParam(value = "objectId", required = false) String objectId,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "Find objects based on catalog") @Valid @RequestParam(value = "catalog", required = false) Catalog catalog,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "Find object by it's name") @Valid @RequestParam(value = "name", required = false) String name,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "Filter by coordinates") @Valid @Nullable Coordinates coordinates, @DecimalMin("0")
                                                                   @Parameter(in = ParameterIn.QUERY, description = "Find objects based on min magnitude" ,schema=@Schema( defaultValue="0")) @Valid @RequestParam(value = "minMag", required = false, defaultValue="0") Float minMag, @DecimalMax("15")
                                                                   @Parameter(in = ParameterIn.QUERY, description = "Find objects based on max magnitude" ,schema=@Schema( defaultValue="15")) @Valid @RequestParam(value = "maxMag", required = false, defaultValue="15") Float maxMag);

    @Operation(summary = "Find space object by ID and catalog", description = "Returns a space object with fluxes", tags={ "SpaceObject", "Flux" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FluxUserTime.class))),

            @ApiResponse(responseCode = "400", description = "Invalid catalog or ID supplied"),

            @ApiResponse(responseCode = "404", description = "Space object not found") })
    @RequestMapping(value = "/api/spaceObject",
            produces = { "application/json", "application/xml" },
            method = RequestMethod.GET)
    ResponseEntity<SpaceObjectWithFluxes> getSpaceObjectById(
            @Parameter(in = ParameterIn.QUERY, description = "ID of space object to return", required=true) @Valid @RequestParam(value = "spaceObjectId") String spaceObjectId,
            @Parameter(in = ParameterIn.QUERY, description = "Catalog of space object to return") @Valid @RequestParam(value = "catalog", required = false) Catalog catalog);


}

