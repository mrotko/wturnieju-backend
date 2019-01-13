package pl.wturnieju.service;

import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableRow;

public interface ITournamentPresentationService {

    TournamentTable<TournamentTableRow> getTournamentTable(String tournamentId);

}
