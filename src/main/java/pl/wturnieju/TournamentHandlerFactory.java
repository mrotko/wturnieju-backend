package pl.wturnieju;

import java.util.Optional;

import pl.wturnieju.model.ChessTournament;
import pl.wturnieju.model.generic.Tournament;


public class TournamentHandlerFactory {

    public static <T extends TournamentState> Optional<TournamentHandler<T>> getInstance(Tournament tournament) {
        switch (tournament.getCompetitionType()) {
        case CHESS:
            return Optional.of(createChessTournamentHandler((ChessTournament) tournament));
        default:
            return Optional.empty();
        }
    }

    private static <T extends TournamentState> ChessTournamentHandler<T> createChessTournamentHandler(ChessTournament tournament) {
        return new ChessTournamentHandler<>(tournament);
    }
}
