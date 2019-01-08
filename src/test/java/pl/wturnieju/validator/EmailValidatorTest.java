package pl.wturnieju.validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.wturnieju.exception.ValidationException;

class EmailValidatorTest {

    private static final List<String> invalidInput = Arrays.asList(
            "",
            "email",
            "@email",
            "email@",
            "email@.com",
            "@.com",
            "@test.com"
    );

    private static final List<String> validInput = Collections.singletonList("email@email.com");

    private EmailValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new EmailValidator();
    }

    @Test
    void validate() {
        validInput.forEach(input -> Assertions.assertTrue(validator.validate(input), input));
        invalidInput.forEach(input -> Assertions.assertFalse(validator.validate(input), input));
    }

    @Test
    void validateAndThrowInvalid() {
        validInput.forEach(input -> Assertions
                .assertDoesNotThrow(() -> validator.validateAndThrowInvalid(input), input));
        invalidInput.forEach(input -> Assertions
                .assertThrows(ValidationException.class, () -> validator.validateAndThrowInvalid(input), input));
    }
}