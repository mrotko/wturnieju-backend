package pl.wturnieju.validator;

public interface IValidator<T> {

    boolean validate(T tested);
}
