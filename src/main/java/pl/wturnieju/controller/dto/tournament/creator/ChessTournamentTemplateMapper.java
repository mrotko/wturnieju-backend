package pl.wturnieju.controller.dto.tournament.creator;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import pl.wturnieju.tournament.ChessTournament;

@Mapper(componentModel = "spring", config = TournamentTemplateMapperConfig.class)
public interface ChessTournamentTemplateMapper {

    @InheritConfiguration(name = "mapToTournament")
    ChessTournament mapToChessTournament(ChessTournamentTemplateDto dto);
}
