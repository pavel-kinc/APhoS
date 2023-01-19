package cz.muni.aphos.openapi;

import com.fasterxml.jackson.annotation.JsonView;
import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.helper.ViewField;
import cz.muni.aphos.openapi.models.*;
import cz.muni.aphos.dto.SpaceObject;
//import io.swagger.model.SpaceObjectWithFluxes;
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
import org.springdoc.api.ErrorMessage;
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
                        array = @ArraySchema(schema = @Schema(implementation = ObjectFluxCount.class)))),

        @ApiResponse(responseCode = "400", description = "Invalid values",
                content = @Content(schema = @Schema(implementation = ErrorMessage.class))) })
    @RequestMapping(value = "api/spaceObject/findByParams",
        produces = { "application/json", "application/xml" },
        method = RequestMethod.GET)
    ResponseEntity<List<ObjectFluxCount>> findSpaceObjectsByParams(
            @Parameter(in = ParameterIn.QUERY, description = "Find object based on it's ID in given catalog") @Valid @RequestParam(value = "objectId", required = false) String objectId,
            @Parameter() @Valid @RequestParam(required = false) Catalog catalog,
            @Parameter(in = ParameterIn.QUERY, description = "Find object by it's name") @Valid @RequestParam(value = "name", required = false) String name,
            @Parameter(in = ParameterIn.QUERY, description = "Filter by coordinates") @Nullable @Valid Coordinates coordinates, @DecimalMin("0")
            @Parameter(in = ParameterIn.QUERY, description = "Find objects based on min magnitude" ,schema=@Schema( defaultValue="0")) @Valid @RequestParam(value = "minMag", required = false, defaultValue="0") Float minMag, @DecimalMax("15")
            @Parameter(in = ParameterIn.QUERY, description = "Find objects based on max magnitude" ,schema=@Schema( defaultValue="15")) @Valid @RequestParam(value = "maxMag", required = false, defaultValue="15") Float maxMag);

    @Operation(summary = "Find space object by ID and catalog", description = "Returns a space object with fluxes, maximum fluxes count is 2000", tags={ "SpaceObject", "Flux" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Flux.class))),

            @ApiResponse(responseCode = "400", description = "Invalid catalog or ID supplied",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),

            @ApiResponse(responseCode = "404", description = "Space object not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))) })
    @RequestMapping(value = "/api/spaceObject/find",
            produces = { "application/json", "application/xml" },
            method = RequestMethod.GET)
    ResponseEntity<SpaceObjectWithFluxes> getSpaceObjectById(
            @Parameter() @Valid @RequestParam() String spaceObjectId,
            @Parameter() @Valid @RequestParam(required = false) Catalog catalog);

    @Operation(summary = "Comparison object of 2 SpaceObjects", description = "Returns a fluxes comparison object, maximum fluxes count is 2000", tags={ "SpaceObject", "Flux" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FluxUserTime.class))),

            @ApiResponse(responseCode = "400", description = "Invalid catalogs or ID supplied",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),

            @ApiResponse(responseCode = "404", description = "Space object not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))) })
    @RequestMapping(value = "/api/spaceObject/comparison",
            produces = { "application/json", "application/xml" },
            method = RequestMethod.GET)
    ResponseEntity<ComparisonObject> getComparisonByIdentificators(
            @Parameter() @Valid @RequestParam() String originalId,
            @Parameter() @Valid @RequestParam(required = false) Catalog originalCat,
            @Parameter() @Valid @RequestParam() String referenceId,
            @Parameter() @Valid @RequestParam(required = false) Catalog referenceCat);
}

