package pl.wturnieju.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.ImmutablePair;

import pl.wturnieju.gamefixture.RoundRobinGameFixture;
import pl.wturnieju.gamefixture.RoundRobinGameFixtureFactory;
import pl.wturnieju.tournament.system.TournamentSystem;
import pl.wturnieju.tournament.system.state.SystemState;

public class RoundRobinScheduleEditor extends ScheduleEditor<RoundRobinGameFixture> {

    public RoundRobinScheduleEditor(TournamentSystem<SystemState<RoundRobinGameFixture>> tournamentSystem) {
        super(tournamentSystem, new RoundRobinGameFixtureFactory());
    }

    @Override
    protected List<ImmutablePair<String, String>> getExcludedPairs() {
        List<ImmutablePair<String, String>> excludedPairs = new ArrayList<>();

        excludedPairs.addAll(getParticipantsPlayedEachOtherPairs());
        excludedPairs.addAll(getParticipantsWithByAsNullOpponent());

        return excludedPairs;
    }

    @Override
    protected BiFunction<String, String, Double> getWeightCalculationMethod() {
        return (a, b) -> 0.;
    }
}
