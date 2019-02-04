package pl.wturnieju.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameFixtureFactory;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.graph.CompleteGraph;
import pl.wturnieju.graph.GraphFactory;
import pl.wturnieju.graph.Vertex;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.system.TournamentSystem;
import pl.wturnieju.tournament.system.state.Group;
import pl.wturnieju.tournament.system.state.SystemStateManager;
import pl.wturnieju.utils.DateUtils;

@RequiredArgsConstructor
public abstract class ScheduleEditor implements IScheduleEditor {

    protected final TournamentSystem tournamentSystem;

    protected final GameFixtureFactory gameFixtureFactory;

    @Override
    public GameFixture updateGame(GameFixture gameFixture) {
        updateGames(Collections.singletonList(gameFixture));
        return gameFixture;
    }

    @Override
    public List<GameFixture> updateGames(List<GameFixture> gameFixtures) {
        var state = tournamentSystem.getSystemState();
        var manager = new SystemStateManager(state);

        gameFixtures.forEach(manager::updateGame);

        tournamentSystem.getStateService().updateSystemState(state);

        return gameFixtures;
    }

    @Override
    public List<GameFixture> addGames(List<GameFixture> gameFixtures) {
        var state = tournamentSystem.getSystemState();
        var manager = new SystemStateManager(state);

        updateGamesWithBye(gameFixtures);
        manager.addGames(gameFixtures);

        tournamentSystem.getStateService().updateSystemState(state);

        return gameFixtures;
    }

    protected void updateGamesWithBye(List<GameFixture> gameFixtures) {
        gameFixtures.stream()
                .filter(GameFixture::getBye)
                .forEach(game -> {
                    var winner = game.getHomeParticipantId() != null
                            ? 1
                            : 2;

                    game.setGameStatus(GameStatus.ENDED);
                    game.setWinner(winner);
                    game.setFinishedDate(DateUtils.getLatest(Timestamp.now(), game.getStartDate()));
                });
    }

    @Override
    public List<String> deleteGames(List<String> gamesIds) {
        var state = tournamentSystem.getSystemState();
        var manager = new SystemStateManager(state);

        manager.deleteGames(gamesIds);

        tournamentSystem.getStateService().updateSystemState(state);

        return gamesIds;
    }

    @Override
    public List<GameFixture> generateGames(String groupId) {
        var state = tournamentSystem.getSystemState();
        var manager = new SystemStateManager(state);
        var group = manager.getGroupById(groupId);
        var graph = new CompleteGraph<>(getWeightCalculationMethod(), new GraphFactory<>());
        var participantsIds = getParticipantsIdsForGamesGeneration(group);

        graph.generateGraph(participantsIds);
        graph.unlinkVertexesWithValues(getCompetitors(group));

        graph.makeFinalSetup();
        graph.findShortestPath();

        List<String> shortestPath = graph.getShortestPath().stream()
                .map(Vertex::getValue)
                .collect(Collectors.toList());

        List<GameFixture> games = new ArrayList<>();
        if (shortestPath.isEmpty()) {
            var edges = graph.getEdges();
            if (!edges.isEmpty() && (edges.size() == participantsIds.size() / 2)) {
                List<String> path = new ArrayList<>();
                edges.forEach(edge -> {
                    path.add(edge.getFirst().getValue());
                    path.add(edge.getSecond().getValue());
                });
                games.addAll(createGameFixtures(path));
            }
        } else {
            games.addAll(createGameFixtures(shortestPath));
        }

        state.getGeneratedGameFixtures().addAll(games);
        tournamentSystem.getStateService().updateSystemState(state);
        return games;
    }

    protected List<ImmutablePair<String, String>> getCompetitors(Group group) {
        return group.getAllGames().stream()
                .filter(GameFixture::getBye)
                .map(game -> ImmutablePair.of(game.getHomeParticipantId(), game.getAwayParticipantId()))
                .collect(Collectors.toList());
    }

    private List<GameFixture> createGameFixtures(List<String> shortestPath) {
        var games = new ArrayList<GameFixture>();
        for (int i = 0; i < shortestPath.size(); i += 2) {
            var homeId = shortestPath.get(i);
            var awayId = shortestPath.get(i + 1);

            var home = getParticipantById(homeId);
            var away = getParticipantById(awayId);

            if (home == null) {
                games.add(gameFixtureFactory.createGameFixture(away, null));
            } else {
                games.add(gameFixtureFactory.createGameFixture(home, away));
            }
        }

        int currentRound = tournamentSystem.getTournament().getCurrentRound() + 1;
        games.forEach(game -> game.setRound(currentRound));

        return games;
    }

    private List<String> getTournamentParticipantsIds(Group group) {
        return group.getParticipants().stream()
                .map(Participant::getId)
                .collect(Collectors.toList());
    }

    private List<String> getParticipantsIdsForGamesGeneration(Group group) {
        List<String> ids = new ArrayList<>(getTournamentParticipantsIds(group));

        if (ids.size() % 2 != 0) {
            ids.add(null);
        }

        return ids;
    }

    @Override
    public List<GameFixture> getGeneratedGames(List<String> gamesIds) {
        return tournamentSystem.getSystemState().getGeneratedGameFixtures().stream()
                .filter(g -> gamesIds.contains(g.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> deleteGeneratedGames(List<String> gamesIds) {
        var state = tournamentSystem.getSystemState();

        state.getGeneratedGameFixtures().removeIf(g -> gamesIds.contains(g.getId()));
        tournamentSystem.getStateService().updateSystemState(state);

        return gamesIds;
    }

    private Participant getParticipantById(String participantId) {
        if (participantId == null) {
            return null;
        }
        return tournamentSystem.getTournament().getParticipants().stream()
                .filter(p -> p.getId().equals(participantId))
                .findFirst().orElse(null);
    }

    protected abstract BiFunction<String, String, Double> getWeightCalculationMethod();
}
