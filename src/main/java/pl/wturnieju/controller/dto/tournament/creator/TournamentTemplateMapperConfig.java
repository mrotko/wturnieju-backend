package pl.wturnieju.controller.dto.tournament.creator;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

import pl.wturnieju.tournament.Tournament;

@MapperConfig(componentModel = "spring")
public interface TournamentTemplateMapperConfig {


    @Mapping(target = "status", constant = "BEFORE_START")
    @Mapping(target = "currentRound", constant = "0")
    @Mapping(source = "maxParticipants", target = "requirements.maxParticipants")
    @Mapping(source = "minParticipants", target = "requirements.minParticipants")
    Tournament mapToTournament(TournamentTemplateDto dto);
}
