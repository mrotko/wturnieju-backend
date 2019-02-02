package pl.wturnieju.schedule;

import java.util.List;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.RoundRobinGameFixture;
import pl.wturnieju.tournament.system.RoundRobinTournamentSystem;

@RequiredArgsConstructor
public class RoundRobinScheduleEditor extends ScheduleEditor<RoundRobinGameFixture> {

    private final RoundRobinTournamentSystem tournamentSystem;

    @Override
    public RoundRobinGameFixture updateGame(RoundRobinGameFixture gameFixture) {
        return null;
    }

    @Override
    public List<RoundRobinGameFixture> updateGames(List<RoundRobinGameFixture> gameFixtures) {
        return null;
    }

    @Override
    public RoundRobinGameFixture addGame(RoundRobinGameFixture gameFixture) {
        return null;
    }

    @Override
    public List<RoundRobinGameFixture> addGames(List<RoundRobinGameFixture> gameFixtures) {
        return null;
    }

    @Override
    public String deleteGame(String gameId) {
        return null;
    }

    @Override
    public List<String> deleteGames(List<String> gamesIds) {
        return null;
    }

    @Override
    public List<RoundRobinGameFixture> generateGames() {
        return null;
    }

    @Override
    public List<RoundRobinGameFixture> getGeneratedGames(List<String> gamesIds) {
        return null;
    }

    @Override
    public List<String> deleteGeneratedGames(List<String> gamesIds) {
        return null;
    }
}
