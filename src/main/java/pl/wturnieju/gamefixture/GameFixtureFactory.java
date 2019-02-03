package pl.wturnieju.gamefixture;

import java.util.Collections;

import org.bson.types.ObjectId;

import pl.wturnieju.tournament.Participant;

public abstract class GameFixtureFactory<T extends GameFixture> {

    abstract public T createGameFixture(Participant participant, Participant opponent);

    protected void initGameFixture(
            GameFixture game,
            Participant participant,
            Participant opponent) {
        game.setId(new ObjectId().toString());
        game.setBye(isBye(participant, opponent));
        game.setStartDate(null);
        game.setEndDate(null);
        game.setFinishedDate(null);
        game.setShortDate(null);
        game.setHomeParticipant(participant);
        game.setAwayParticipant(opponent);
        game.setHomeScore(createScore());
        game.setAwayScore(createScore());
        game.setGameStatus(GameStatus.BEFORE_START);
        game.setWinner(0);
        game.setRound(null);
        game.setStage(null);
    }

    private boolean isBye(Participant participant, Participant opponent) {
        return opponent == null || participant == null;
    }

    private Score createScore() {
        var score = new Score();

        score.setCurrent(null);
        score.setPeriods(Collections.emptyMap());

        return score;
    }
}
