package pl.wturnieju.exception;

public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException(String message) {
        super(message);
    }

    public ResourceExistsException() {
    }
}
