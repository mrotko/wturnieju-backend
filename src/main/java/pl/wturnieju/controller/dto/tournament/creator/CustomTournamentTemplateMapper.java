package pl.wturnieju.controller.dto.tournament.creator;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import pl.wturnieju.tournament.CustomTournament;

@Mapper(componentModel = "spring", config = TournamentTemplateMapperConfig.class)
public interface CustomTournamentTemplateMapper {

    @InheritConfiguration(name = "mapToTournament")
    CustomTournament mapToCustomTournament(CustomTournamentTemplateDto dto);
}
