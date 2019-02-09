package pl.wturnieju.controller.dto.auth;

import java.util.List;

import org.mapstruct.Mapper;

import pl.wturnieju.model.PasswordPatternGroupType;

@Mapper(componentModel = "spring")
public interface PasswordPatternGroupMapper {

    default PasswordPatternGroupDto mapToPasswordPatternGroupDto(PasswordPatternGroupType passwordPatternGroupType) {
        if (passwordPatternGroupType == null) {
            return null;
        }

        var dto = new PasswordPatternGroupDto();

        dto.setPattern(passwordPatternGroupType.getPattern());
        dto.setPatternGroupType(passwordPatternGroupType);

        return dto;
    }

    List<PasswordPatternGroupDto> mapToPasswordPatternGroupDtos(List<PasswordPatternGroupType> passwordPatternGroupTypes);
}
