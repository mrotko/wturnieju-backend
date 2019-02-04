package pl.wturnieju.service;

import java.util.List;

import pl.wturnieju.gamefixture.GameFixture;

public interface IGeneratedGamesService {

    void deleteAllByIds(List<String> gameFixturesIds);

    void deleteById(String gameFixtureId);

    List<GameFixture> insertAll(List<GameFixture> gameFixtures);

    List<GameFixture> getAllByIds(List<String> gameFixtureIds);
}
