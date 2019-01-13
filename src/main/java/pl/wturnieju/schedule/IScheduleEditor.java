package pl.wturnieju.schedule;

import java.util.List;

import pl.wturnieju.gamefixture.GameFixture;

public interface IScheduleEditor<T extends GameFixture> {

    T updateGame(T gameFixture);

    List<T> updateGames(List<T> gameFixtures);

    T addGame(T gameFixture);

    List<T> addGames(List<T> gameFixtures);

    String deleteGame(String gameId);

    List<String> deleteGames(List<String> gamesIds);

    List<T> generateGames();

    List<T> getGeneratedGames(List<String> gamesIds);

    List<String> deleteGeneratedGames(List<String> gamesIds);
}
