package app.helloworld;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HelloExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(HelloExceptionHandler.class);

    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleIOException() {
        logger.error("Not found");
    }
}
