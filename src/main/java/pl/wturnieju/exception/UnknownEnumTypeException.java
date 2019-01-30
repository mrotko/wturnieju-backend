package pl.wturnieju.exception;

public class UnknownEnumTypeException extends RuntimeException {

    public UnknownEnumTypeException() {
    }

    public UnknownEnumTypeException(Enum type) {
        super(type == null ? "null: " : type.name());
    }
}
