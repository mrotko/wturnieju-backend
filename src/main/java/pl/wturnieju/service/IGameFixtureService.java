package pl.wturnieju.service;

import java.util.List;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.Timestamp;

public interface IGameFixtureService {

    GameFixture update(GameFixture gameFixture);

    List<GameFixture> updateAll(List<GameFixture> gameFixtures);

    GameFixture save(GameFixture gameFixture);

    List<GameFixture> saveAll(List<GameFixture> gameFixtures);

    void deleteAllByIds(List<String> gameFixturesIds);

    void deleteById(String gameFixtureId);

    List<GameFixture> getAllByGroupId(String groupId);

    GameFixture getById(String gameFixtureId);

    List<GameFixture> getAllByTournamentId(String tournamentId);

    List<GameFixture> getAllPublicStartsBetweenDates(Timestamp dateFrom, Timestamp dateTo);
}
