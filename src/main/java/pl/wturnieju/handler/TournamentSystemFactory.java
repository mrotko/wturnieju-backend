package pl.wturnieju.handler;

import pl.wturnieju.model.TournamentSystemType;

public class TournamentSystemFactory {

    public static TournamentSystem getInstance(TournamentSystemType type) {
        switch (type) {
        case SWISS:
            return createSwissSystem();
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + type);
        }
    }

    private static TournamentSystem createSwissSystem() {
        return new SwissTournamentSystem();
    }
}
