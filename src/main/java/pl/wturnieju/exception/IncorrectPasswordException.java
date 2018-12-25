package pl.wturnieju.exception;

public class
IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String message) {
        super(message);
    }

    public IncorrectPasswordException() {
    }
}
