package pl.wturnieju.tournament.system;


import org.springframework.context.ApplicationContext;

import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.Tournament;

public class TournamentSystemFactory {

    public static TournamentSystem create(ApplicationContext context, Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return new SwissTournamentSystem(
                    getGameFixtureService(context),
                    getGroupService(context),
                    getParticipantService(context),
                    tournament);
        case ROUND_ROBIN:
            return new RoundRobinTournamentSystem(
                    getGameFixtureService(context),
                    getGroupService(context),
                    getParticipantService(context),
                    tournament);
        case KNOCKOUT:
            return new KnockOutTournamentSystem(
                    getGameFixtureService(context),
                    getGroupService(context),
                    getParticipantService(context),
                    tournament);
        case GROUP:
            return new GroupTournamentSystem(
                    getGameFixtureService(context),
                    getGroupService(context),
                    getParticipantService(context),
                    tournament);
        case LEAGUE:
            return new LeagueTournamentSystem(
                    getGameFixtureService(context),
                    getGroupService(context),
                    getParticipantService(context),
                    tournament);
        case CUSTOM:
            return new CustomTournamentSystem(
                    getGameFixtureService(context),
                    getGroupService(context),
                    getParticipantService(context),
                    tournament);
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + tournament.getSystemType());
        }
    }

    private static IGroupService getGroupService(ApplicationContext context) {
        return context.getBean(IGroupService.class);
    }

    private static IGameFixtureService getGameFixtureService(ApplicationContext context) {
        return context.getBean(IGameFixtureService.class);
    }

    private static IParticipantService getParticipantService(ApplicationContext context) {
        return context.getBean(IParticipantService.class);
    }
}
