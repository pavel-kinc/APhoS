package cz.muni.aphos.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for API to make correct return values and objects.
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
    protected ResponseEntity<ErrorMessage> handleException(ResponseStatusException e){
        ErrorMessage error = new ErrorMessage(e.getReason());
        if(e.getStatusCode() != HttpStatus.NOT_FOUND){
            log.error(error.getId() + error.getMessage());
        }
        return new ResponseEntity<>(new ErrorMessage(e.getReason()), e.getStatusCode());
    }
}
