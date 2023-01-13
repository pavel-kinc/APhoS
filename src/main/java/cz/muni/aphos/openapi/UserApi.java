package cz.muni.aphos.openapi;


//import cz.muni.aphos.openapi.models.User;
import cz.muni.aphos.dto.User;
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
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Validated
public interface UserApi {

    @Operation(summary = "Find user by username", description = "Returns a user", tags={ "User" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful operation",
                content = @Content(schema = @Schema(implementation = User.class))),
        
        @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                content = @Content(schema = @Schema(implementation = ErrorMessage.class))),

        @ApiResponse(responseCode = "404", description = "User not found",
                content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @RequestMapping(value = "/findByUsername",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.GET)
    ResponseEntity<User> getUserByUsername(@NotBlank @Parameter(in = ParameterIn.QUERY, required=true)
                                           @Valid @RequestParam(value = "username") String username);

}

