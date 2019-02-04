package pl.wturnieju.gamefixture;

import java.util.Collections;

import org.bson.types.ObjectId;

import pl.wturnieju.tournament.Participant;

public class GameFixtureFactory {

    public GameFixture createGameFixture(Participant participant, Participant opponent) {
        var game = new GameFixture();

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
        game.setStageType(null);

        return game;
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
