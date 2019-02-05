package pl.wturnieju.controller.dto.search;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.wturnieju.tournament.Tournament;

@Mapper(componentModel = "spring")
public interface TournamentSearchDtoMapper {

    @Mapping(source = "id", target = "tournamentId")
    @Mapping(source = "name", target = "tournamentName")
    TournamentSearchDto mapToTournamentSearchDto(Tournament tournament);

    List<TournamentSearchDto> mapToTournamentSearchDtos(List<Tournament> tournaments);
}
