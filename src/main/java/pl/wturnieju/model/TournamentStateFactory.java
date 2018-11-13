package pl.wturnieju.model;

import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentSystemState;
import pl.wturnieju.model.swiss.SwissSystemState;

public class TournamentStateFactory {

    public static TournamentSystemState getInstance(Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return createSwissTournamentState();
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + tournament.getSystemType());
        }
    }

    private static SwissSystemState createSwissTournamentState() {
        return new SwissSystemState();
    }

}
