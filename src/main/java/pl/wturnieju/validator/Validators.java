package pl.wturnieju.validator;

public class Validators {

    public static IValidator<String> getEmailValidator() {
        return new EmailValidator();
    }

    public static IValidator<String> getPasswordValidator() {
        return new PasswordValidator();
    }
}
