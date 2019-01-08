package pl.wturnieju.validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.wturnieju.exception.ValidationException;

@ExtendWith(SpringExtension.class)
class PasswordValidatorTest {

    private Map<PasswordType, String> passwords;

    private PasswordValidator validator;

    @BeforeEach
    public void setUp() {
        passwords = new HashMap<>();
        passwords.put(PasswordType.LETTER, "a");
        passwords.put(PasswordType.LETTER_NUMBER, "a1");
        passwords.put(PasswordType.LETTER_NUMBER_SPECIAL, "a1.");
        passwords.put(PasswordType.BIG_LETTER_NUMBER_SPECIAL, "Aa1.");
        passwords.put(PasswordType.LETTER_NUMBER_8_LEN_SPECIAL, "a1.asdfa");
        passwords.put(PasswordType.BIG_LETTER_NUMBER_8_SPECIAL, "Aa1.asdf");
        validator = new PasswordValidator();
    }

    protected List<String> getValidInput() {
        return Collections.singletonList(passwords.get(PasswordType.BIG_LETTER_NUMBER_8_SPECIAL));
    }

    protected List<String> getInvalidInput() {
        return passwords.entrySet().stream()
                .filter(p -> !getValidInput().contains(p.getValue()))
                .map(Entry::getValue)
                .collect(Collectors.toList());
    }

    @Test
    void validate() {
        getValidInput().forEach(input -> Assertions.assertTrue(validator.validate(input), input));
        getInvalidInput().forEach(input -> Assertions.assertFalse(validator.validate(input), input));
    }

    @Test
    void validateAndThrowInvalid() {
        getValidInput().forEach(input -> Assertions
                .assertDoesNotThrow(() -> validator.validateAndThrowInvalid(input), input));
        getInvalidInput().forEach(input -> Assertions
                .assertThrows(ValidationException.class, () -> validator.validateAndThrowInvalid(input), input));
    }

    private enum PasswordType {
        LETTER,
        LETTER_NUMBER,
        LETTER_NUMBER_SPECIAL,
        BIG_LETTER_NUMBER_SPECIAL,
        LETTER_NUMBER_8_LEN_SPECIAL,
        BIG_LETTER_NUMBER_8_SPECIAL,
    }
}