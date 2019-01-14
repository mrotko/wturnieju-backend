package pl.wturnieju.tournament.system;


import org.springframework.context.ApplicationContext;

import pl.wturnieju.service.impl.SwissSystemStateService;
import pl.wturnieju.tournament.Tournament;

public class TournamentSystemFactory {

    public static TournamentSystem create(ApplicationContext context, Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return new SwissTournamentSystem(getSwissStateService(context), tournament);
        //        case ROUND_ROBIN:
        //            return createRoundRobinSystem(tournament);
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + tournament.getSystemType());
        }
    }

    private static SwissSystemStateService getSwissStateService(ApplicationContext context) {
        return context.getBean(SwissSystemStateService.class);
    }
}
