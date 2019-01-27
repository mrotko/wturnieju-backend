package pl.wturnieju.controller.dto.tournament.schedule;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.service.ITournamentService;

@Mapper(componentModel = "spring",
        uses = {ScheduleElementDtoMapper.class}
)
public abstract class ScheduleDtoMapper {

    @Autowired
    protected ITournamentService tournamentService;

    @Mapping(source = "tournamentId", target = "tournamentId")
    @Mapping(source = "round", target = "round")
    @Mapping(source = "gameFixtures", target = "elements")
    public abstract ScheduleDto toScheduleDto(
            String tournamentId,
            Integer round,
            List<GameFixture> gameFixtures
    );

    @AfterMapping
    protected ScheduleDto setTournamentName(@MappingTarget ScheduleDto dto) {
        var tournamentName = tournamentService.getTournamentName(dto.getTournamentId());
        dto.setTournamentName(tournamentName);
        return dto;
    }
}
