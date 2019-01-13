package pl.wturnieju.controller.dto.schedule;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pl.wturnieju.controller.dto.TeamDtoMapper;
import pl.wturnieju.gamefixture.GameFixture;

@Mapper(componentModel = "spring", uses = {TeamDtoMapper.class})
public interface ScheduleElementDtoMapper {

    @Mapping(source = "id", target = "gameId")
    ScheduleElementDto gameFixtureToScheduleElementDto(GameFixture source);

    @Mapping(source = "gameId", target = "id")
    GameFixture scheduleElementDtoToGameFixture(ScheduleElementDto source);

    GameFixture updateGameFixture(ScheduleElementDto dto, @MappingTarget GameFixture gameFixture);
}
