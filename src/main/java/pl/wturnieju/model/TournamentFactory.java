package pl.wturnieju.model;

import pl.wturnieju.tournament.ChessTournament;
import pl.wturnieju.tournament.Tournament;

public class TournamentFactory {

    public static Tournament getTournament(CompetitionType competitionType) {
        try {
            switch (competitionType) {
            case CHESS:
                return new ChessTournament();
            default:
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(
                    "Unknown competition type inside " + TournamentFactory.class
                            .getSimpleName() + ": " + competitionType);
        }
    }
}
