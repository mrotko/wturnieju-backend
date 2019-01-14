package pl.wturnieju.controller.dto.tournament;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.wturnieju.tournament.Tournament;

@Mapper(componentModel = "spring", uses = {TournamentParticipantDtoMapper.class})
public interface TournamentDtoMapper {

    @Mapping(source = "tournamentParticipantType", target = "participantType")
    TournamentDto tournamentToTournamentDto(Tournament tournament);

    List<TournamentDto> tournamentToTournamentDtos(List<Tournament> tournament);
}
