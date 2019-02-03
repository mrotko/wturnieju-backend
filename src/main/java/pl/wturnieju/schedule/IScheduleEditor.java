package pl.wturnieju.schedule;

import java.util.List;

import pl.wturnieju.gamefixture.GameFixture;

public interface IScheduleEditor {

    GameFixture updateGame(GameFixture gameFixture);

    List<GameFixture> updateGames(List<GameFixture> gameFixtures);

    List<GameFixture> addGames(List<GameFixture> gameFixtures);

    List<String> deleteGames(List<String> gamesIds);

    List<GameFixture> generateGames();

    List<GameFixture> getGeneratedGames(List<String> gamesIds);

    List<String> deleteGeneratedGames(List<String> gamesIds);
}
