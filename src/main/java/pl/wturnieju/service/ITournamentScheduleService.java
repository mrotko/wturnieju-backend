package pl.wturnieju.service;

import java.util.List;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.Timestamp;

public interface ITournamentScheduleService {

    List<GameFixture> generateSchedule(String tournamentId, String groupId);

    List<GameFixture> getAllGameFixtures(String tournamentId);

    List<GameFixture> getGameFixturesBeforeStart(String tournamentId);

    List<GameFixture> getEndedGameFixtures(String tournamentId);

    GameFixture updateGameFixture(String tournamentId, GameFixture gameFixture);

    List<GameFixture> saveSchedule(String tournamentId, List<GameFixture> games);

    List<GameFixture> getGeneratedSchedule(String tournamentId, List<String> gameIds);

    List<GameFixture> getGameFixturesBetweenDates(String tournamentId, Timestamp dateFrom, Timestamp dateTo);
}
