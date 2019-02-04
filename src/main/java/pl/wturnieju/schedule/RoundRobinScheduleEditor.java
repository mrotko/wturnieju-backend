package pl.wturnieju.schedule;

import java.util.function.BiFunction;

import pl.wturnieju.gamefixture.GameFixtureFactory;
import pl.wturnieju.tournament.system.TournamentSystem;

public class RoundRobinScheduleEditor extends ScheduleEditor {

    public RoundRobinScheduleEditor(TournamentSystem tournamentSystem) {
        super(tournamentSystem, new GameFixtureFactory());
    }

    @Override
    protected BiFunction<String, String, Double> getWeightCalculationMethod() {
        return (a, b) -> 0.;
    }
}
