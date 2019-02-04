package pl.wturnieju.service;

import java.util.List;
import java.util.Optional;

import pl.wturnieju.model.AccessOption;
import pl.wturnieju.tournament.Tournament;

public interface ITournamentService {

    void startTournament(String tournamentId);

    void finishTournament(String tournamentId);

    Tournament getById(String tournamentId);

    Tournament getByIdOrThrow(String tournamentId);

    Optional<Tournament> findTournament(String tournamentId);

    void deleteTournament(String tournamentId);

    List<Tournament> getUserTournaments(String userId);

    void updateTournament(Tournament tournament);

    List<Tournament> getTournamentsByAccess(AccessOption accessOption);

    String getTournamentName(String tournamentId);
}
