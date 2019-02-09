package pl.wturnieju.service;

import pl.wturnieju.validator.IValidator;

public interface IValidatorService {

    IValidator<String> getPasswordValidator();

    IValidator<String> getEmailValidator();
}
