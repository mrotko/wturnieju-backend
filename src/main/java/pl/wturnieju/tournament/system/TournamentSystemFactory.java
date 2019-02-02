package pl.wturnieju.tournament.system;


import org.springframework.context.ApplicationContext;

import pl.wturnieju.service.impl.CustomSystemStateService;
import pl.wturnieju.service.impl.GroupSystemStateService;
import pl.wturnieju.service.impl.KnockOutSystemStateService;
import pl.wturnieju.service.impl.LeagueSystemStateService;
import pl.wturnieju.service.impl.RoundRobinSystemStateService;
import pl.wturnieju.service.impl.SwissSystemStateService;
import pl.wturnieju.tournament.Tournament;

public class TournamentSystemFactory {

    public static TournamentSystem create(ApplicationContext context, Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return new SwissTournamentSystem(getSwissStateService(context), tournament);
        case ROUND_ROBIN:
            return new RoundRobinTournamentSystem(getRoundRobinSystemStateService(context), tournament);
        case KNOCKOUT:
            return new KnockOutTournamentSystem(getKnockOutSystemStateService(context), tournament);
        case GROUP:
            return new GroupTournamentSystem(getGroupSystemStateService(context), tournament);
        case LEAGUE:
            return new LeagueTournamentSystem(getLeagueSystemStateService(context), tournament);
        case CUSTOM:
            return new CustomTournamentSystem(getCustomSystemStateService(context), tournament);
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + tournament.getSystemType());
        }
    }

    private static SwissSystemStateService getSwissStateService(ApplicationContext context) {
        return context.getBean(SwissSystemStateService.class);
    }

    private static RoundRobinSystemStateService getRoundRobinSystemStateService(ApplicationContext context) {
        return context.getBean(RoundRobinSystemStateService.class);
    }

    private static KnockOutSystemStateService getKnockOutSystemStateService(ApplicationContext context) {
        return context.getBean(KnockOutSystemStateService.class);
    }

    private static GroupSystemStateService getGroupSystemStateService(ApplicationContext context) {
        return context.getBean(GroupSystemStateService.class);
    }

    private static LeagueSystemStateService getLeagueSystemStateService(ApplicationContext context) {
        return context.getBean(LeagueSystemStateService.class);
    }

    private static CustomSystemStateService getCustomSystemStateService(ApplicationContext context) {
        return context.getBean(CustomSystemStateService.class);
    }
}
