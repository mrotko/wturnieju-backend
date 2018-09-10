package pl.wturnieju.model;

import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentState;
import pl.wturnieju.model.swiss.SwissState;

public class TournamentStateFactory {

    public static TournamentState getInstance(Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return createSwissTournamentState(tournament);
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + tournament.getSystemType());
        }
    }

    private static SwissState createSwissTournamentState(Tournament tournament) {
        return new SwissState(tournament);
    }

}
