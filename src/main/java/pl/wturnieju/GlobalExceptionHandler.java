package pl.wturnieju;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import pl.wturnieju.exception.IncorrectPasswordException;
import pl.wturnieju.exception.UserNotFoundException;
import pl.wturnieju.exception.ValueExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ValueExistsException.class,
            UserNotFoundException.class,
            IncorrectPasswordException.class,
            UsernameNotFoundException.class
    })
    public final ResponseEntity<String> handleException(Exception e, WebRequest request) {
        if (e instanceof ValueExistsException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } else if (e instanceof UserNotFoundException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } else if (e instanceof IncorrectPasswordException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } else if (e instanceof UsernameNotFoundException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
