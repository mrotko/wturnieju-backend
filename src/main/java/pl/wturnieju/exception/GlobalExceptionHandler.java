package pl.wturnieju.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ResourceExistsException.class,
            UserNotFoundException.class,
            IncorrectPasswordException.class,
            InvalidFormatException.class,
    })
    public final ResponseEntity<String> handleException(Exception e, WebRequest request) {
        if (e instanceof ResourceExistsException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } else if (e instanceof UserNotFoundException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } else if (e instanceof IncorrectPasswordException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (e instanceof InvalidFormatException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
