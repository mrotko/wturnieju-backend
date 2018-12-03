package pl.wturnieju.handler;

import pl.wturnieju.model.generic.RoundRobinSystemState;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.swiss.SwissSystemState;

public class TournamentSystemFactory {

    public static TournamentSystem getInstance(Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return createSwissSystem(tournament);
        case ROUND_ROBIN:
            return createRoundRobinSystem(tournament);
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + tournament.getSystemType());
        }
    }

    private static RoundRobinTournamentSystem createRoundRobinSystem(Tournament tournament) {
        var system = new RoundRobinTournamentSystem();

        system.setState((RoundRobinSystemState) tournament.getTournamentSystemState());
        system.setTournament(tournament);

        return system;
    }

    private static SwissTournamentSystem createSwissSystem(Tournament tournament) {
        var swissSystem = new SwissTournamentSystem();

        swissSystem.setState((SwissSystemState) tournament.getTournamentSystemState());
        swissSystem.setTournament(tournament);

        return swissSystem;
    }
}
