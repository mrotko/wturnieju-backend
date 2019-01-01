package pl.wturnieju.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.TournamentParticipant;
import pl.wturnieju.model.TournamentStatus;
import pl.wturnieju.model.generic.GenericFixtureUpdateBundle;
import pl.wturnieju.model.generic.GenericTournamentTable;
import pl.wturnieju.model.generic.Tournament;


// TODO(mr): 12.11.2018 impl serwisu do dashboard
/*
 * url tournament/{id}/dashboard
 * invite CRUD playersIds
 * invite staff
 * start next round / end tournament
 * get tournament schedule / results
 * get leaderboard - should be service for stats
 * CRUD messages
 * user permissions for crud operations
 *
 * */


// TODO(mr): 12.11.2018 imp serwisu do zarządzania turniejem
/*
 *
 * check permissions
 * start tournament
 * finish round
 * finish tournament
 * get standings
 *
 *
 * */
// TODO(mr): 15.11.2018 after tournament end can be created some cache of stats

public interface ITournamentService {

    void updateTournament(Tournament tournament);

    void updateTournament(GenericTournamentUpdateBundle bundle);

    void updateFixture(GenericFixtureUpdateBundle bundle);

    Optional<Tournament> getById(String tournamentId);

    Map<TournamentStatus, List<Tournament>> getAllUserTournamentsGroupedByStatus(String userId);

    Fixture getFixtureById(String tournamentId, String fixtureId);

    List<Fixture> getFixtures(String tournamentId);

    GenericTournamentTable getTournamentTable(String tournamentId);

    List<Fixture> getCurrentFixtures(String tournamentId);

    List<Fixture> prepareNextRound(String tournamentId);

    void addNextRoundFixtures(String tournamentId, List<Fixture> fixtures);

    Optional<Integer> getCurrentRound(String tournamentId);

    List<Tournament> getUserTournaments(String userId);

    Optional<TournamentParticipant> findParticipantByUserId(String tournamentId, String userId);
}
