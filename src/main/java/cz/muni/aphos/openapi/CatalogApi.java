package cz.muni.aphos.openapi;

import cz.muni.aphos.openapi.models.Catalog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Interface for Catalog with annotations for API controllers and swagger-ui.
 * Basic pathing is in @RequestMapping.
 */
@RequestMapping("/api/catalogs")
public interface CatalogApi {

    /**
     * Gets all allowable catalogs.
     *
     * @return List of catalogs
     */
    @Operation(summary = "Find all catalogs", description = "Returns catalogs", tags = {"Catalog"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Catalog.class)))),

            @ApiResponse(responseCode = "400", description = "Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @RequestMapping(value = "",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<String[]> getCatalogs();
}
