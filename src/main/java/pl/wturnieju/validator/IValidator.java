package pl.wturnieju.validator;

import pl.wturnieju.exception.ValidationException;

public interface IValidator<T> {

    boolean validate(T tested);

    void validateAndThrowInvalid(T tested) throws ValidationException;
}
