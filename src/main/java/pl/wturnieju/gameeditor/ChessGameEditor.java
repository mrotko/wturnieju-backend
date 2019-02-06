package pl.wturnieju.gameeditor;

import java.util.Collections;

import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.Score;

public class ChessGameEditor extends GameEditor {

    public ChessGameEditor(GameFixture gameFixture) {
        super(gameFixture);
    }

    @Override
    public GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent) {
        var homeScore = new Score();
        var awayScore = new Score();

        if (finishGameUpdateEvent.getWinner() == 1) {
            homeScore.setPeriods(Collections.emptyMap());
            homeScore.setCurrent(1.);

            awayScore.setPeriods(Collections.emptyMap());
            awayScore.setCurrent(0.);
        } else if (finishGameUpdateEvent.getWinner() == 2) {
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
        finishGameUpdateEvent.setHomeScore(homeScore);
        finishGameUpdateEvent.setAwayScore(awayScore);

        return super.finishGame(finishGameUpdateEvent);
    }
}
