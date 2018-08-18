package pl.wturnieju.exception;

public class ValueExistsException extends RuntimeException {
    public ValueExistsException(String message) {
        super(message);
    }

    public ValueExistsException() {
    }
}
