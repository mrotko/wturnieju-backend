package pl.wturnieju.controller.dto.tournament;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import pl.wturnieju.service.IGroupService;
import pl.wturnieju.tournament.system.state.Group;

@Mapper(componentModel = "spring", uses = {ParticipantDtoMapper.class})
public abstract class GroupDtoMapper {

    @Autowired
    protected IGroupService groupService;

    public abstract GroupDto mapToGroupDto(Group group);

    public GroupDto mapToGroupDto(String groupId) {
        var group = groupService.getById(groupId);
        return mapToGroupDto(group);
    }

    public abstract List<GroupDto> mapToGroupDtos(List<String> groupIds);
}
