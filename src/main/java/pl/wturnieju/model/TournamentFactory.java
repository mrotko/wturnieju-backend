package pl.wturnieju.model;

import pl.wturnieju.tournament.ChessTournament;
import pl.wturnieju.tournament.CustomTournament;
import pl.wturnieju.tournament.FootballTournament;
import pl.wturnieju.tournament.TennisTournament;
import pl.wturnieju.tournament.Tournament;

public class TournamentFactory {

    public static Tournament getTournament(CompetitionType competitionType) {
        try {
            switch (competitionType) {
            case CHESS:
                return new ChessTournament();
            case FOOTBALL:
                return new FootballTournament();
            case TENNIS:
                return new TennisTournament();
            case CUSTOM:
                return new CustomTournament();
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
