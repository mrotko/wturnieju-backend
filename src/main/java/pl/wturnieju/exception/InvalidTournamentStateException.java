package pl.wturnieju.exception;

public class InvalidTournamentStateException extends RuntimeException {

    public InvalidTournamentStateException() {
        super();
    }

    public InvalidTournamentStateException(String message) {
        super(message);
    }

    public InvalidTournamentStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTournamentStateException(Throwable cause) {
        super(cause);
    }
}
