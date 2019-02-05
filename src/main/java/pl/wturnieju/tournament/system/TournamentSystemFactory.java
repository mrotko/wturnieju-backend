package pl.wturnieju.tournament.system;


import org.springframework.context.ApplicationContext;

import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.service.ITournamentTableService;
import pl.wturnieju.tournament.Tournament;

public class TournamentSystemFactory {

    public static TournamentSystem create(ApplicationContext context, Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return new SwissTournamentSystem(
                    getTournamentTableService(context),
                    getGameFixtureService(context),
                    getGroupService(context),
                    getParticipantService(context),
                    tournament);
        case ROUND_ROBIN:
            return new RoundRobinTournamentSystem(
                    getTournamentTableService(context),
                    getGameFixtureService(context),
                    getGroupService(context),
                    getParticipantService(context),
                    tournament);
        case KNOCKOUT:
            //            return new KnockOutTournamentSystem(
            //                    getTournamentTableService(context),
            //                    getGameFixtureService(context),
            //                    getGroupService(context),
            //                    getParticipantService(context),
            //                    tournament);
            return null;
        case GROUP:
            //            return new GroupTournamentSystem(
            //                    getTournamentTableService(context),
            //                    getGameFixtureService(context),
            //                    getGroupService(context),
            //                    getParticipantService(context),
            //                    tournament);
            return null;
        case LEAGUE:
            return new LeagueTournamentSystem(
                    getTournamentTableService(context),
                    getGameFixtureService(context),
                    getGroupService(context),
                    getParticipantService(context),
                    tournament);
        case CUSTOM:
            //            return new CustomTournamentSystem(
            //                    getTournamentTableService(context),
            //                    getGameFixtureService(context),
            //                    getGroupService(context),
            //                    getParticipantService(context),
            //                    tournament);
            return null;
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

    private static ITournamentTableService getTournamentTableService(ApplicationContext context) {
        return context.getBean(ITournamentTableService.class);
    }
}
