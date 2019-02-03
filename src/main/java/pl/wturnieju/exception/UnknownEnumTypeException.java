package pl.wturnieju.exception;

public class UnknownEnumTypeException extends RuntimeException {

    public UnknownEnumTypeException() {
    }

    public UnknownEnumTypeException(Enum type) {
        super("Unknown enum type: " + (type == null ? "null" : type.name()));
    }
}
