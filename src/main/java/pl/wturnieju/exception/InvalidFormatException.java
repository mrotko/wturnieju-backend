package pl.wturnieju.exception;

public class InvalidFormatException extends RuntimeException {
    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException() {
    }
}
