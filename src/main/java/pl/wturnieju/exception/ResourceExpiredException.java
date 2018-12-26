package pl.wturnieju.exception;

public class ResourceExpiredException extends RuntimeException {

    public ResourceExpiredException() {
    }

    public ResourceExpiredException(String message) {
        super(message);
    }
}
