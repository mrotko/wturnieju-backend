package pl.wturnieju.controller.dto.tournament;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.wturnieju.tournament.Tournament;

@Mapper(componentModel = "spring", uses = {ParticipantDtoMapper.class})
public interface TournamentDtoMapper {

    @Mapping(source = "requirements.minParticipants", target = "minParticipants")
    @Mapping(source = "requirements.maxParticipants", target = "maxParticipants")
    @Mapping(source = "requirements.plannedRounds", target = "plannedRounds")
    TournamentDto tournamentToTournamentDto(Tournament tournament);

    List<TournamentDto> tournamentToTournamentDtos(List<Tournament> tournament);
}
