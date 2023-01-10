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


import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserApiController implements UserApi {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    public UserApiController(HttpServletRequest request) {
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(ResponseStatusException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getReason()), e.getStatus());
    }

    public ResponseEntity<User> getUserByUsername(@NotBlank @Parameter(in = ParameterIn.QUERY, required=true)
                                  @Valid @RequestParam(value = "username") String username) {
        User user;
        try{
            user = userRepo.findByUsername(username);
        } catch (Exception e){
            log.error("User endpoint problem", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
