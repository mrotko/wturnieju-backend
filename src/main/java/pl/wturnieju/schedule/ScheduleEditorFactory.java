package pl.wturnieju.schedule;

import pl.wturnieju.tournament.system.SwissTournamentSystem;
import pl.wturnieju.tournament.system.TournamentSystem;

public class ScheduleEditorFactory {

    public static IScheduleEditor create(TournamentSystem tournamentSystem) {
        switch (tournamentSystem.getTournament().getSystemType()) {
        case SWISS:
            return new SwissScheduleEditor((SwissTournamentSystem) tournamentSystem);
        default:
            throw new IllegalArgumentException("Unknown enum " + tournamentSystem.getTournament().getSystemType());
        }
    }
}
