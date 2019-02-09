package pl.wturnieju.controller.dto.auth;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthConfigurationDto {

    private List<PasswordPatternGroupDto> passwordPatternGroups;
}
