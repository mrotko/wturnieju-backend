package pl.wturnieju.controller.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import pl.wturnieju.gamefixture.Team;

@Mapper(componentModel = "spring")
public interface TeamDtoMapper {

    TeamDto teamToTeamDto(Team source);

    Team teamDtoToTeam(TeamDto source);

    Team updateTeam(TeamDto dto, @MappingTarget Team team);
}
