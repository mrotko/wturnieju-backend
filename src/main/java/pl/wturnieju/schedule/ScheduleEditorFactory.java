package pl.wturnieju.schedule;

import org.springframework.context.ApplicationContext;

import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGeneratedGamesService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.service.ITournamentTableService;
import pl.wturnieju.tournament.Tournament;

public class ScheduleEditorFactory {

    public static IScheduleEditor create(ApplicationContext context,
            Tournament tournament) {
        var systemType = tournament.getSystemType();

        return switch (systemType) {
            case SWISS -> new SwissScheduleEditor(
                    getTournamentTableService(context),
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
            case KNOCKOUT -> new KnockOutScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
            case GROUP -> new GroupScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
            case LEAGUE -> new LeagueScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
            case ROUND_ROBIN -> new RoundRobinScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
            case CUSTOM -> new CustomScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
        };
    }

    private static IGeneratedGamesService getGeneratedGamesService(ApplicationContext context) {
        return context.getBean(IGeneratedGamesService.class);
    }

    private static IGameFixtureService getGameFixtureService(ApplicationContext context) {
        return context.getBean(IGameFixtureService.class);
    }

    private static IGroupService groupService(ApplicationContext context) {
        return context.getBean(IGroupService.class);
    }

    private static IParticipantService getParticipantService(ApplicationContext context) {
        return context.getBean(IParticipantService.class);
    }

    private static ITournamentTableService getTournamentTableService(ApplicationContext context) {
        return context.getBean(ITournamentTableService.class);
    }
}
