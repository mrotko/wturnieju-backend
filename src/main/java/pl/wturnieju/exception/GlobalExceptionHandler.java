package pl.wturnieju.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import pl.wturnieju.controller.dto.ExceptionErrorDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ResourceExistsException.class,
            UserNotFoundException.class,
            IncorrectPasswordException.class,
            InvalidFormatException.class,
            TournamentAccessDeniedException.class,
            ResourceExpiredException.class
    })
    public final ResponseEntity<ExceptionErrorDTO> handleException(Exception e, WebRequest request) {
        if (e instanceof ResourceExistsException) {
            return new ResponseEntity<>(createDto(e), HttpStatus.CONFLICT);
        } else if (e instanceof UserNotFoundException) {
            return new ResponseEntity<>(createDto(e), HttpStatus.BAD_REQUEST);
        } else if (e instanceof IncorrectPasswordException) {
            return new ResponseEntity<>(createDto(e), HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (e instanceof InvalidFormatException) {
            return new ResponseEntity<>(createDto(e), HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (e instanceof ResourceExpiredException) {
            return new ResponseEntity<>(createDto(e), HttpStatus.GONE);
        } else if (e instanceof TournamentAccessDeniedException) {
            return new ResponseEntity<>(createDto(e), HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ExceptionErrorDTO createDto(Exception e) {
        var dto = new ExceptionErrorDTO();
        dto.setMessage(e.getMessage());
        dto.setSimpleClassName(e.getClass().getSimpleName());
        return dto;
    }
}
