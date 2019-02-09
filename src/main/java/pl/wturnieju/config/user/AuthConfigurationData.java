package pl.wturnieju.config.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.PasswordPatternGroupType;

@Getter
@Setter
public class AuthConfigurationData {
    private List<PasswordPatternGroupType> passwordPatternGroupTypes;
}
