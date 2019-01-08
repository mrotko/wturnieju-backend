package pl.wturnieju.validator;

import java.util.regex.Pattern;

import pl.wturnieju.exception.ValidationException;

public class PatternValidator implements IValidator<String> {

    private final Pattern pattern;

    public PatternValidator(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public boolean validate(String input) {
        try {
            validateAndThrowInvalid(input);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void validateAndThrowInvalid(String input) throws ValidationException {
        var result = pattern.matcher(input).matches();
        if (!result) {
            throw new ValidationException("Input not match to pattern. Input [" + input + "]");
        }
    }
}
