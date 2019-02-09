package pl.wturnieju.controller.dto.auth;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.PasswordPatternGroupType;

@Getter
@Setter
public class PasswordPatternGroupDto {

    private PasswordPatternGroupType patternGroupType;

    private String pattern;
}
