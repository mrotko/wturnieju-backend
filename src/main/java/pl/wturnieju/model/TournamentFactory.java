package pl.wturnieju.model;

import pl.wturnieju.model.generic.Tournament;

public class TournamentFactory {

    public static Tournament getTournament(CompetitionType competitionType) {

        switch (competitionType) {
        case CHESS:
            return new ChessTournament();
        default:
            throw new IllegalArgumentException(
                    "Unknown competition type inside factory" + TournamentFactory.class.getSimpleName());
        }
    }
}
