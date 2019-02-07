package pl.wturnieju.service;

import java.util.List;
import java.util.Optional;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.tournament.LegType;

public interface IGameFixtureService {

    GameFixture update(GameFixture gameFixture);

    List<GameFixture> updateAll(List<GameFixture> gameFixtures);

    GameFixture save(GameFixture gameFixture);

    List<GameFixture> saveAll(List<GameFixture> gameFixtures);

    void deleteAllByIds(List<String> gameFixturesIds);

    void deleteById(String gameFixtureId);

    List<GameFixture> getAllByGroupId(String groupId);

    List<GameFixture> getAllByGroupIdAndLegType(String groupId, LegType legType);

    List<GameFixture> getAllPendingGamesByGroupId(String groupId);

    Integer countPendingGamesByGroupId(String groupId);

    List<GameFixture> getAllEndedByGroupId(String groupId);

    GameFixture getById(String gameFixtureId);

    List<GameFixture> getAllByTournamentId(String tournamentId);

    List<GameFixture> getAllPublicStartsBetweenDates(Timestamp dateFrom, Timestamp dateTo);

    Optional<GameFixture> findLastParticipantsGame(String firstParticipantId, String secondParticipantId);

}
