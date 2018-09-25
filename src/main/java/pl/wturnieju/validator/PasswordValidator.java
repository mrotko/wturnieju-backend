package pl.wturnieju.validator;

import java.util.regex.Pattern;

public class PasswordValidator implements IValidator<String> {

    //    8 chars, 1 letter, 1 number
    private static final String REGEX_PATTERN_0 = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d].{8,}$";

    private final Pattern pattern;

    public PasswordValidator() {
        pattern = Pattern.compile(REGEX_PATTERN_0);
    }

    @Override
    public boolean validate(String password) {
        return pattern.matcher(password).matches();
    }
}
