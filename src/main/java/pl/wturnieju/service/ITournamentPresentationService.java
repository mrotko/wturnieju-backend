package pl.wturnieju.service;

import pl.wturnieju.tournament.system.table.TournamentTable;

public interface ITournamentPresentationService {

    TournamentTable getTournamentTable(String tournamentId);

}
