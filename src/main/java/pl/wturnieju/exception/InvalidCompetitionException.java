package pl.wturnieju.exception;

public class InvalidCompetitionException extends RuntimeException {
    public InvalidCompetitionException() {
        super();
    }

    public InvalidCompetitionException(String message) {
        super(message);
    }

    public InvalidCompetitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCompetitionException(Throwable cause) {
        super(cause);
    }
}
