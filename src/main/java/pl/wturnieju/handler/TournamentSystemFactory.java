package pl.wturnieju.handler;

import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.swiss.SwissState;

public class TournamentSystemFactory {

    public static TournamentSystem getInstance(Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            var system = createSwissSystem();
            system.setState((SwissState) tournament.getTournamentState());
            return system;
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + tournament.getSystemType());
        }
    }

    private static SwissTournamentSystem createSwissSystem() {
        return new SwissTournamentSystem();
    }
}
