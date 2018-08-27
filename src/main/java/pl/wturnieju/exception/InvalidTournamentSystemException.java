package pl.wturnieju.exception;

public class InvalidTournamentSystemException extends RuntimeException {

    public InvalidTournamentSystemException() {
    }

    public InvalidTournamentSystemException(String message) {

        super(message);
    }

    public InvalidTournamentSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTournamentSystemException(Throwable cause) {
        super(cause);
    }
}
