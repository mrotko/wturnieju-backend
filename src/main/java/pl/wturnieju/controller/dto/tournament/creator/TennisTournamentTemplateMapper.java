package pl.wturnieju.controller.dto.tournament.creator;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import pl.wturnieju.tournament.TennisTournament;

@Mapper(componentModel = "spring", config = TournamentTemplateMapperConfig.class)
public interface TennisTournamentTemplateMapper {

    @InheritConfiguration(name = "mapToTournament")
    TennisTournament mapToTennisTournament(TennisTournamentTemplateDto dto);
}
