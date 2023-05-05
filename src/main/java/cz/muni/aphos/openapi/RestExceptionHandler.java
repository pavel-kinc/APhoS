package cz.muni.aphos.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for API to make correct return values and objects.
 * ResponseEntityExceptionHandler handles exceptions from Spring MVC exceptions
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Handle ResponseStatusException.
     *
     * @param e exception
     * @return ErrorMessage in response entity
     */
    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<ErrorMessage> handleException(ResponseStatusException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorMessage(e.getReason()), e.getStatusCode());
    }
}
