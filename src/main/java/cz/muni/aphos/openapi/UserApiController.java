package cz.muni.aphos.openapi;


import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.aphos.dto.User;
import cz.muni.aphos.dao.UserRepo;
//import cz.muni.aphos.openapi.models.User;
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
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class UserApiController implements UserApi {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    //private final HttpServletRequest request;

    private final UserRepo userRepo;

    @Autowired
    public UserApiController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getUserByUsername(@NotBlank @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "username", required = true) String username) {
        try{
            User user = userRepo.findByUsername(username);
            if(user == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return user;
        } catch (Exception e){
            log.error("User endpoint problem", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
