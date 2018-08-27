package pl.wturnieju;

import java.util.Optional;

import pl.wturnieju.model.TournamentSystemType;

public class TournamentSystemFactory {

    public static Optional<TournamentSystem> getInstance(TournamentSystemType type) {
        switch (type) {
        case SWISS:
            return Optional.of(createSwissSystem());
        default:
            return Optional.empty();
        }
    }

    private static TournamentSystem createSwissSystem() {
        return new SwissTournamentSystem();
    }
}
