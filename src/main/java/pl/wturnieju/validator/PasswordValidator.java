package pl.wturnieju.validator;

import java.util.regex.Pattern;

import pl.wturnieju.exception.ValidationException;

public class PasswordValidator implements IValidator<String> {

    //    8 chars, 1 letter, 1 number
    private static final String REGEX_PATTERN_0 = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d].{8,}$";

    private final Pattern pattern;

    public PasswordValidator() {
        pattern = Pattern.compile(REGEX_PATTERN_0);
    }

    @Override
    public boolean validate(String password) {
        try {
            validateAndThrowInvalid(password);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void validateAndThrowInvalid(String password) throws ValidationException {
        var result = pattern.matcher(password).matches();
        if (!result) {
            throw new ValidationException("Invalid password format");
        }
    }
}
