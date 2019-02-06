package pl.wturnieju.gameeditor;


import lombok.RequiredArgsConstructor;
import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;

@RequiredArgsConstructor
public abstract class GameEditor implements IGameEditor {

    protected final GameFixture gameFixture;

    @Override
    public GameFixture startGame(StartGameUpdateEvent startGameUpdateEvent) {
        gameFixture.setStartDate(startGameUpdateEvent.getStartDate());
        gameFixture.setGameStatus(GameStatus.IN_PROGRESS);

        return gameFixture;
    }

    @Override
    public GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent) {
        gameFixture.setGameStatus(GameStatus.ENDED);
        gameFixture.setFinishedDate(finishGameUpdateEvent.getFinishDate());
        gameFixture.setWinner(finishGameUpdateEvent.getWinner());
        gameFixture.setHomeScore(finishGameUpdateEvent.getHomeScore());
        gameFixture.setAwayScore(finishGameUpdateEvent.getAwayScore());

        return gameFixture;
    }
}
