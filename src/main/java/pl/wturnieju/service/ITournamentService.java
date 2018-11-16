package pl.wturnieju.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.generic.GenericFixtureUpdateBundle;
import pl.wturnieju.model.generic.Tournament;

public interface ITournamentService {

    void updateTournament(Tournament tournament);

    void updateTournament(GenericTournamentUpdateBundle bundle);

    void updateFixture(GenericFixtureUpdateBundle bundle);

    Optional<Tournament> getById(String tournamentId);

    Map<TournamentStatus, List<Tournament>> getAllUserTournamentsGroupedByStatus(String userId);
}
