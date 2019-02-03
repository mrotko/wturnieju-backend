package pl.wturnieju.tournament.system;


import org.springframework.context.ApplicationContext;

import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.service.impl.SystemStateService;
import pl.wturnieju.tournament.Tournament;

public class TournamentSystemFactory {

    public static TournamentSystem create(ApplicationContext context, Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return new SwissTournamentSystem(getSystemStateService(context), tournament);
        case ROUND_ROBIN:
            return new RoundRobinTournamentSystem(getSystemStateService(context), tournament);
        case KNOCKOUT:
            return new KnockOutTournamentSystem(getSystemStateService(context), tournament);
        case GROUP:
            return new GroupTournamentSystem(getSystemStateService(context), tournament);
        case LEAGUE:
            return new LeagueTournamentSystem(getSystemStateService(context), tournament);
        case CUSTOM:
            return new CustomTournamentSystem(getSystemStateService(context), tournament);
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + tournament.getSystemType());
        }
    }

    private static ISystemStateService getSystemStateService(ApplicationContext context) {
        return context.getBean(SystemStateService.class);
    }
}
