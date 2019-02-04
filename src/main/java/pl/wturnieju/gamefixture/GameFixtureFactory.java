package pl.wturnieju.gamefixture;

import java.util.Collections;

import pl.wturnieju.model.Timestamp;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.Group;

public class GameFixtureFactory {

    public GameFixture createGameFixture(
            Tournament tournament,
            Group group,
            Participant participant, Participant opponent) {
        var game = new GameFixture();

        game.setGroupId(group.getId());
        game.setTournamentId(tournament.getId());
        game.setPreviousGameFixtureId(null);
        game.setStartDate(Timestamp.now());
        game.setEndDate(Timestamp.now());
        game.setFinishedDate(null);
        game.setShortDate(true);
        game.setHomeParticipant(participant);
        game.setHomeScore(createScore());
        game.setAwayParticipant(opponent);
        game.setAwayScore(createScore());
        game.setGameStatus(GameStatus.BEFORE_START);
        game.setWinner(initWinner(participant, opponent));
        game.setRound(null);
        game.setBye(isBye(participant, opponent));
        game.setLive(false);
        game.setLegType(tournament.getCurrentLegType());
        game.setStageType(group.getStageType());
        game.setAccessOption(tournament.getAccessOption());
        game.setCompetitionType(tournament.getCompetitionType());

        return game;
    }

    private int initWinner(Participant participant, Participant opponent) {
        if (participant != null && opponent != null) {
            return 0;
        }
        if (participant == null) {
            return 2;
        }
        return 1;
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
