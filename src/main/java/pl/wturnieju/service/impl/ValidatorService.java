package pl.wturnieju.service.impl;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.config.user.AuthConfiguration;
import pl.wturnieju.model.PasswordPatternGroupType;
import pl.wturnieju.service.IValidatorService;
import pl.wturnieju.validator.EmailValidator;
import pl.wturnieju.validator.IValidator;
import pl.wturnieju.validator.PasswordValidator;

@Service
@RequiredArgsConstructor
public class ValidatorService implements IValidatorService {

    private final AuthConfiguration authConfiguration;

    @Override
    public IValidator<String> getPasswordValidator() {
        var patternGroupTypes = authConfiguration.getAuthConfigurationData().getPasswordPatternGroupTypes();
        var pattern = patternGroupTypes.stream()
                .map(PasswordPatternGroupType::getPattern)
                .collect(Collectors.joining(""));
        return new PasswordValidator(pattern);
    }

    @Override
    public IValidator<String> getEmailValidator() {
        return new EmailValidator();
    }
}
