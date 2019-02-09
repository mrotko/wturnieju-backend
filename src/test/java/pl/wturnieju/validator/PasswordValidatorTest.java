package pl.wturnieju.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pl.wturnieju.config.user.AuthConfiguration;
import pl.wturnieju.exception.ValidationException;
import pl.wturnieju.service.IValidatorService;
import pl.wturnieju.service.impl.ValidatorService;

@Import(value = {AuthConfiguration.class, ValidatorService.class})
@ExtendWith(SpringExtension.class)
class PasswordValidatorTest {

    private Map<PasswordType, String> passwords;

    @Autowired
    private IValidatorService validatorService;

    @BeforeEach
    public void setUp() {
        passwords = new HashMap<>();
        passwords.put(PasswordType.LETTER, "a");
        passwords.put(PasswordType.LETTER_NUMBER, "a1");
        passwords.put(PasswordType.LETTER_NUMBER_SPECIAL, "a1.");
        passwords.put(PasswordType.BIG_LETTER_NUMBER_SPECIAL, "Aa1.");
        passwords.put(PasswordType.LETTER_NUMBER_8_LEN_SPECIAL, "a1.asdfa");
        passwords.put(PasswordType.BIG_LETTER_SMALL_LETTER_NUMBER_8, "Aa8aaaaa");
        passwords.put(PasswordType.BIG_LETTER_SMALL_LETTER_NUMBER_8_SPECIAL, "Aa1.asdf");
    }

    protected List<String> getValidInput() {
        return List.of(
                passwords.get(PasswordType.BIG_LETTER_SMALL_LETTER_NUMBER_8_SPECIAL),
                passwords.get(PasswordType.BIG_LETTER_SMALL_LETTER_NUMBER_8));
    }

    protected List<String> getInvalidInput() {
        return passwords.entrySet().stream()
                .filter(p -> !getValidInput().contains(p.getValue()))
                .map(Entry::getValue)
                .collect(Collectors.toList());
    }

    @Test
    void validate() {
        getValidInput().forEach(input -> Assertions.assertTrue(getPasswordValidator().validate(input), input));
        getInvalidInput().forEach(input -> Assertions.assertFalse(getPasswordValidator().validate(input), input));
    }

    @Test
    void validateAndThrowInvalid() {
        getValidInput().forEach(input -> Assertions
                .assertDoesNotThrow(() -> getPasswordValidator().validateAndThrowInvalid(input), input));
        getInvalidInput().forEach(input -> Assertions
                .assertThrows(ValidationException.class, () -> getPasswordValidator().validateAndThrowInvalid(input),
                        input));
    }

    private IValidator<String> getPasswordValidator() {
        return validatorService.getPasswordValidator();
    }

    private enum PasswordType {
        LETTER,
        LETTER_NUMBER,
        LETTER_NUMBER_SPECIAL,
        BIG_LETTER_NUMBER_SPECIAL,
        LETTER_NUMBER_8_LEN_SPECIAL,
        BIG_LETTER_SMALL_LETTER_NUMBER_8,
        BIG_LETTER_SMALL_LETTER_NUMBER_8_SPECIAL,
    }
}