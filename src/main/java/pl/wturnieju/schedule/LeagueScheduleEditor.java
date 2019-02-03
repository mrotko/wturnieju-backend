package pl.wturnieju.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.ImmutablePair;

import pl.wturnieju.gamefixture.LeagueGameFixture;
import pl.wturnieju.gamefixture.LeagueGameFixtureFactory;
import pl.wturnieju.tournament.system.TournamentSystem;
import pl.wturnieju.tournament.system.state.SystemState;

public class LeagueScheduleEditor extends ScheduleEditor<LeagueGameFixture> {

    public LeagueScheduleEditor(TournamentSystem<SystemState<LeagueGameFixture>> tournamentSystem) {
        super(tournamentSystem, new LeagueGameFixtureFactory());
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
