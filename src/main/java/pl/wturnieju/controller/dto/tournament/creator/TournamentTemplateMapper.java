package pl.wturnieju.controller.dto.tournament.creator;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.wturnieju.tournament.Tournament;

@Mapper(componentModel = "spring")
public interface TournamentTemplateMapper {

    @Mapping(target = "status", constant = "BEFORE_START")
    @Mapping(target = "currentRound", constant = "0")
    @Mapping(source = "maxParticipants", target = "requirements.maxParticipants")
    @Mapping(source = "minParticipants", target = "requirements.minParticipants")
    @Mapping(source = "requiredAllGamesEndedStageTypes", target = "requirements.requiredAllGamesEndedStageTypes")
    Tournament mapToTournament(TournamentTemplateDto dto);
}
