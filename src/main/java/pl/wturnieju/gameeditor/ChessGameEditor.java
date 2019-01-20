package pl.wturnieju.gameeditor;

import java.util.Collections;

import pl.wturnieju.gameeditor.finish.FinishChessGameUpdateEventImpl;
import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartChessGameUpdateEventImpl;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.gamefixture.Score;

public class ChessGameEditor extends GameEditor {

    public ChessGameEditor(GameFixture gameFixture) {
        super(gameFixture);
    }

    @Override
    public GameFixture startGame(StartGameUpdateEvent startGameUpdateEvent) {
        var update = (StartChessGameUpdateEventImpl) startGameUpdateEvent;
        gameFixture.setStartDate(update.getStartDate());
        gameFixture.setGameStatus(GameStatus.IN_PROGRESS);
        return gameFixture;
    }

    @Override
    public GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent) {
        var update = (FinishChessGameUpdateEventImpl) finishGameUpdateEvent;

        gameFixture.setGameStatus(GameStatus.ENDED);
        gameFixture.setFinishedDate(update.getFinishDate());
        gameFixture.setWinner(update.getWinner());

        var homeScore = new Score();
        var awayScore = new Score();

        if (update.getWinner() == 1) {
            homeScore.setPeriods(Collections.emptyMap());
            homeScore.setCurrent(1.);

            awayScore.setPeriods(Collections.emptyMap());
            awayScore.setCurrent(0.);
        } else if (update.getWinner() == 2) {
            awayScore.setPeriods(Collections.emptyMap());
            awayScore.setCurrent(1.);

            homeScore.setPeriods(Collections.emptyMap());
            homeScore.setCurrent(0.);
        } else {
            homeScore.setPeriods(Collections.emptyMap());
            homeScore.setCurrent(0.5);

            awayScore.setPeriods(Collections.emptyMap());
            awayScore.setCurrent(0.5);
        }
        gameFixture.setHomeScore(homeScore);
        gameFixture.setAwayScore(awayScore);

        return gameFixture;
    }
}
