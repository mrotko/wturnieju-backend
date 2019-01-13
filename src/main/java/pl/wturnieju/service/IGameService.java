package pl.wturnieju.service;

import java.util.List;

import pl.wturnieju.gamefixture.GameFixture;

public interface IGameService {

    GameFixture updateGameResult();

    GameFixture updateGameStatus();

    GameFixture deleteGame(String tournamentId, String gameId);

    List<GameFixture> getAllGames();
}
