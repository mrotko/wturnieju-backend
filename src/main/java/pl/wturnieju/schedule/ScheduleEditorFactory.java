package pl.wturnieju.schedule;

import org.springframework.context.ApplicationContext;

import pl.wturnieju.exception.UnknownEnumTypeException;
import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGeneratedGamesService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.Tournament;

public class ScheduleEditorFactory {

    public static IScheduleEditor create(ApplicationContext context,
            Tournament tournament) {
        var systemType = tournament.getSystemType();

        switch (systemType) {
        case SWISS:
            return new SwissScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
        case KNOCKOUT:
            return new KnockOutScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
        case GROUP:
            return new GroupScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
        case LEAGUE:
            return new LeagueScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
        case ROUND_ROBIN:
            return new RoundRobinScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
        case CUSTOM:
            return new CustomScheduleEditor(
                    getParticipantService(context),
                    getGeneratedGamesService(context),
                    getGameFixtureService(context),
                    groupService(context),
                    tournament);
        default:
            throw new UnknownEnumTypeException(systemType);
        }
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
}
