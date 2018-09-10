package pl.wturnieju.handler;

import pl.wturnieju.model.ChessTournament;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentState;


public class TournamentHandlerFactory {

    public static <T extends TournamentState> TournamentHandler<T> getInstance(Tournament tournament) {
        switch (tournament.getCompetitionType()) {
        case CHESS:
            return createChessTournamentHandler((ChessTournament) tournament);
        default:
            throw new IllegalArgumentException(
                    "unknown tournament competition type: " + tournament.getCompetitionType());
        }
    }

    private static <T extends TournamentState> ChessTournamentHandler<T> createChessTournamentHandler(ChessTournament tournament) {
        return new ChessTournamentHandler<>(tournament);
    }
}
