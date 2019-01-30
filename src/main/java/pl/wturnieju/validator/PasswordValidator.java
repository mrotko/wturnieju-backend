package pl.wturnieju.validator;

public class PasswordValidator extends PatternValidator {

    public static final String BIG_LETTER_NUMBER_SPECIAL_8LEN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%,.]).{8,})";

    public static final String BIG_LETTER_NUMBER_8LEN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d].{8,}$";

    public PasswordValidator() {
        super(BIG_LETTER_NUMBER_8LEN);
    }
}
