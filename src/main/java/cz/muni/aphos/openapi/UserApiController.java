package cz.muni.aphos.openapi;


import cz.muni.aphos.dao.UserRepo;
import cz.muni.aphos.dto.User;
import cz.muni.aphos.services.UserService;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserApiController implements UserApi {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    // global handling cannot handle IllegalArgumentException it seems
    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<User> getUserByUsername(@PathVariable(name = "name") @NotBlank String username) {
        User user;
        try {
            user = userRepo.findByUsername(username);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getLoggedUser() {
        try {
            Authentication auth = SecurityContextHolder.
                    getContext().getAuthentication();
            User user = null;
            if (auth != null && !auth.getPrincipal().toString().equals("anonymousUser")) {
                user = userService.getCurrentUser();

            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Logged user endpoint problem");
        }

    }

}
