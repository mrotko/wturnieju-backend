package pl.wturnieju.service;

import pl.wturnieju.tournament.system.table.TournamentTable;

public interface ITournamentTableService {

    TournamentTable getByGroupId(String groupId);

    void deleteByGroupId(String groupId);

    TournamentTable insert(TournamentTable tournamentTable);

    TournamentTable update(TournamentTable tournamentTable);
}
