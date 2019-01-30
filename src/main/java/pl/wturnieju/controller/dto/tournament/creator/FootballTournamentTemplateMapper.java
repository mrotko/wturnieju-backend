package pl.wturnieju.controller.dto.tournament.creator;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import pl.wturnieju.tournament.FootballTournament;

@Mapper(componentModel = "spring", config = TournamentTemplateMapperConfig.class)
public interface FootballTournamentTemplateMapper {

    @InheritConfiguration(name = "mapToTournament")
    FootballTournament mapToFootballTournament(FootballTournamentTemplateDto dto);
}
