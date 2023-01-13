package cz.muni.aphos.openapi;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value =
            {ResponseStatusException.class, IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<ErrorMessage> handleException(ResponseStatusException e){
        return new ResponseEntity<>(new ErrorMessage(e.getReason()), e.getStatusCode());
    }
}
