package pl.wturnieju.controller.dto.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.wturnieju.config.user.AuthConfigurationData;

@Mapper(componentModel = "spring", uses = {PasswordPatternGroupMapper.class})
public interface AuthConfigurationMapper {

    @Mapping(source = "passwordPatternGroupTypes", target = "passwordPatternGroups")
    AuthConfigurationDto mapToAuthConfigurationDto(AuthConfigurationData data);
}
