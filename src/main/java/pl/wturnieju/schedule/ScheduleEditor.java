package pl.wturnieju.schedule;

import java.util.ArrayList;
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
import pl.wturnieju.tournament.system.state.SystemState;
import pl.wturnieju.utils.DateUtils;

@RequiredArgsConstructor
public abstract class ScheduleEditor implements IScheduleEditor {

    protected final TournamentSystem<SystemState> tournamentSystem;

    protected final GameFixtureFactory gameFixtureFactory;

    @Override
    public GameFixture updateGame(GameFixture gameFixture) {
        var state = tournamentSystem.getSystemState();

        state.getGameFixtures().removeIf(game -> game.getId().equals(gameFixture.getId()));
        state.getGameFixtures().add(gameFixture);
        tournamentSystem.getStateService().updateSystemState(state);

        return gameFixture;
    }

    @Override
    public List<GameFixture> updateGames(List<GameFixture> gameFixtures) {
        var state = tournamentSystem.getSystemState();
        var idsToRemove = gameFixtures.stream()
                .map(GameFixture::getId)
                .collect(Collectors.toList());

        state.getGameFixtures().removeIf(game -> idsToRemove.contains(game.getId()));
        state.getGameFixtures().addAll(gameFixtures);
        tournamentSystem.getStateService().updateSystemState(state);

        return gameFixtures;
    }

    @Override
    public List<GameFixture> addGames(List<GameFixture> gameFixtures) {
        var state = tournamentSystem.getSystemState();

        gameFixtures.forEach(game -> {
            if (game.getBye()) {
                var participantId = game.getHomeParticipantId() != null
                        ? game.getHomeParticipantId()
                        : game.getAwayParticipantId();

                var winner = game.getHomeParticipantId() != null
                        ? 1
                        : 2;

                state.getParticipantsWithBye().add(participantId);
                game.setGameStatus(GameStatus.ENDED);
                game.setWinner(winner);
                game.setFinishedDate(DateUtils.getLatest(Timestamp.now(), game.getStartDate()));
            } else {
                state.getParticipantsPlayedEachOther().computeIfAbsent(
                        game.getHomeParticipantId(),
                        k -> new ArrayList<>()
                ).add(game.getAwayParticipantId());

                state.getParticipantsPlayedEachOther().computeIfAbsent(
                        game.getAwayParticipantId(),
                        k -> new ArrayList<>()
                ).add(game.getHomeParticipantId());
            }
        });

        state.getGameFixtures().addAll(gameFixtures);
        tournamentSystem.getStateService().updateSystemState(state);

        return gameFixtures;
    }

    @Override
    public List<String> deleteGames(List<String> gamesIds) {
        var state = tournamentSystem.getSystemState();

        state.getGameFixtures().removeIf(g -> gamesIds.contains(g.getId()));
        tournamentSystem.getStateService().updateSystemState(state);

        return gamesIds;
    }

    @Override
    public List<GameFixture> generateGames() {
        var state = tournamentSystem.getSystemState();
        var graph = new CompleteGraph<>(getWeightCalculationMethod(), new GraphFactory<>());
        var participantsIds = getParticipantsIdsForGamesGeneration();

        graph.generateGraph(participantsIds);
        graph.unlinkVertexesWithValues(getExcludedPairs());

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

        state.setGeneratedGameFixtures(games);
        tournamentSystem.getStateService().updateSystemState(state);
        return games;
    }

    protected List<ImmutablePair<String, String>> getParticipantsWithByAsNullOpponent() {
        return tournamentSystem.getSystemState().getParticipantsWithBye().stream()
                .map(id -> ImmutablePair.of(id, (String) null))
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

        int currentStage = tournamentSystem.getTournament().getCurrentStage();
        games.forEach(game -> game.setStage(currentStage));

        return games;
    }

    protected List<ImmutablePair<String, String>> getParticipantsPlayedEachOtherPairs() {
        return tournamentSystem.getSystemState().getParticipantsPlayedEachOther().entrySet().stream()
                .flatMap(participantIdToPlayedEntry -> participantIdToPlayedEntry.getValue().stream()
                        .map(played -> ImmutablePair.of(participantIdToPlayedEntry.getKey(), played)))
                .collect(Collectors.toList());
    }

    private List<String> getTournamentParticipantsIds() {
        return tournamentSystem.getTournament().getParticipants().stream()
                .map(Participant::getId)
                .collect(Collectors.toList());
    }

    private List<String> getParticipantsIdsForGamesGeneration() {
        List<String> ids = new ArrayList<>(getTournamentParticipantsIds());

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

    protected abstract List<ImmutablePair<String, String>> getExcludedPairs();

    protected abstract BiFunction<String, String, Double> getWeightCalculationMethod();
}
