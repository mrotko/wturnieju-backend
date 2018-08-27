package pl.wturnieju;

import java.util.Optional;

import pl.wturnieju.model.generic.Tournament;

public class TournamentStateFactory {

    public static Optional<TournamentState> getInstance(Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return Optional.of(createSwissTournamentState(tournament));
        default:
            return Optional.empty();
        }
    }

    private static SwissState createSwissTournamentState(Tournament tournament) {
        return new SwissState(tournament);
    }

}
