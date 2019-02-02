package pl.wturnieju.controller.dto.tournament.schedule;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pl.wturnieju.controller.dto.tournament.ParticipantDtoMapper;
import pl.wturnieju.controller.dto.tournament.gamefixture.ScoreDtoMapper;
import pl.wturnieju.gamefixture.GameFixture;

@Mapper(componentModel = "spring", uses = {ParticipantDtoMapper.class, ScoreDtoMapper.class})
public interface ScheduleElementDtoMapper {

    @Mapping(source = "id", target = "gameId")
    ScheduleElementDto gameFixtureToScheduleElementDto(GameFixture source);

    @InheritInverseConfiguration
    GameFixture scheduleElementDtoToGameFixture(ScheduleElementDto source);

    GameFixture updateGameFixture(ScheduleElementDto dto, @MappingTarget GameFixture gameFixture);
}
