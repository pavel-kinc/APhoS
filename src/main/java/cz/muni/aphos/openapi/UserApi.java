package cz.muni.aphos.openapi;


//import cz.muni.aphos.openapi.models.User;

import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.dto.User;
import cz.muni.aphos.openapi.models.Catalog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.websocket.server.PathParam;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Interface for User with annotations for API controllers and swagger-ui.
 * Basic pathing is in @RequestMapping.
 */
@Validated
@RequestMapping("/api/user")
public interface UserApi {

    /**
     * Get user by username in APhoS database - API.
     * More info in annotations.
     *
     * @param username not blank path parameter username
     * @return User in ResponseEntity (json - {username, description}) or other API responses
     */
    @Operation(summary = "Find user by username", description = "Returns a user", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = User.class))),

            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),

            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))

    })
    @RequestMapping(value = "/{name}",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.GET)
    ResponseEntity<User> getUserByUsername(@Parameter @PathVariable(name = "name") @NotBlank String username);

    /**
     * Gets currently logged user
     *
     * @return logged-in user or null in ResponseEntity
     */
    @Operation(summary = "Get current logged-in user for session", description = "Returns a user or null", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = User.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))

    })
    @RequestMapping(value = "/current",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.GET)
    ResponseEntity<User> getLoggedUser();

}

