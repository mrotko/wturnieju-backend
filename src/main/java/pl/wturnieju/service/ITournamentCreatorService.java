package pl.wturnieju.service;

import pl.wturnieju.dto.TournamentTemplateDto;
import pl.wturnieju.tournament.Tournament;

public interface ITournamentCreatorService {

    void update(TournamentTemplateDto tournamentTemplateDto);

    Tournament create(TournamentTemplateDto tournamentTemplateDto);

    TournamentTemplateDto getUserTemplate();
}
