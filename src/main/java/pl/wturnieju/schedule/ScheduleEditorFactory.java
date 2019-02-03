package pl.wturnieju.schedule;

import pl.wturnieju.exception.UnknownEnumTypeException;
import pl.wturnieju.tournament.system.TournamentSystem;

public class ScheduleEditorFactory {

    @SuppressWarnings("unchecked")
    public static IScheduleEditor create(TournamentSystem tournamentSystem) {
        var systemType = tournamentSystem.getTournament().getSystemType();

        switch (systemType) {
        case SWISS:
            return new SwissScheduleEditor(tournamentSystem);
        case KNOCKOUT:
            return new KnockOutScheduleEditor(tournamentSystem);
        case GROUP:
            return new GroupScheduleEditor(tournamentSystem);
        case LEAGUE:
            return new LeagueScheduleEditor(tournamentSystem);
        case ROUND_ROBIN:
            return new RoundRobinScheduleEditor(tournamentSystem);
        case CUSTOM:
            return new CustomScheduleEditor(tournamentSystem);
        default:
            throw new UnknownEnumTypeException(systemType);
        }
    }
}
