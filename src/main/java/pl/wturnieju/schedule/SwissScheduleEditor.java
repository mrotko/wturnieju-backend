package pl.wturnieju.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameFixtureFactory;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.gamefixture.SwissGameFixture;
import pl.wturnieju.gamefixture.SwissGameFixtureFactory;
import pl.wturnieju.graph.CompleteGraph;
import pl.wturnieju.graph.GraphFactory;
import pl.wturnieju.graph.Vertex;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.system.SwissTournamentSystem;

@RequiredArgsConstructor
public class SwissScheduleEditor extends ScheduleEditor<SwissGameFixture> {

    private final SwissTournamentSystem tournamentSystem;

    private GameFixtureFactory<SwissGameFixture> gameFixtureFactory = new SwissGameFixtureFactory();

    @Override
    public SwissGameFixture updateGame(SwissGameFixture gameFixture) {
        var state = tournamentSystem.getSystemState();
        state.getGameFixtures().removeIf(game -> game.getId().equals(gameFixture.getId()));
        state.getGameFixtures().add(gameFixture);
        tournamentSystem.getStateService().updateSystemState(state);
        return gameFixture;
    }

    @Override
    public List<SwissGameFixture> updateGames(List<SwissGameFixture> gameFixtures) {
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
    public SwissGameFixture addGame(SwissGameFixture gameFixture) {
        var state = tournamentSystem.getSystemState();
        state.getGameFixtures().add(gameFixture);
        tournamentSystem.getStateService().updateSystemState(state);

        return gameFixture;
    }

    @Override
    public List<SwissGameFixture> addGames(List<SwissGameFixture> gameFixtures) {
        var state = tournamentSystem.getSystemState();

        gameFixtures.forEach(game -> {
            if (game.getBye()) {
                state.getParticipantsWithBye().add(game.getHomeParticipant().getId());
                game.setGameStatus(GameStatus.ENDED);
                game.setWinner(1);
                game.setFinishedDate(Timestamp.now());
            } else {
                state.getParticipantsPlayedEachOther().computeIfAbsent(
                        game.getHomeParticipant().getId(),
                        k -> new ArrayList<>()
                ).add(game.getAwayParticipant().getId());

                state.getParticipantsPlayedEachOther().computeIfAbsent(
                        game.getAwayParticipant().getId(),
                        k -> new ArrayList<>()
                ).add(game.getHomeParticipant().getId());
            }
        });

        state.getGameFixtures().addAll(gameFixtures);
        tournamentSystem.getStateService().updateSystemState(state);

        return gameFixtures;
    }

    @Override
    public String deleteGame(String gameId) {
        var state = tournamentSystem.getSystemState();

        state.getGameFixtures().removeIf(g -> g.getId().equals(gameId));
        tournamentSystem.getStateService().updateSystemState(state);

        return gameId;
    }

    @Override
    public List<String> deleteGames(List<String> gamesIds) {
        var state = tournamentSystem.getSystemState();

        state.getGameFixtures().removeIf(g -> gamesIds.contains(g.getId()));
        tournamentSystem.getStateService().updateSystemState(state);

        return gamesIds;
    }

    @Override
    public List<SwissGameFixture> generateGames() {
        var state = tournamentSystem.getSystemState();
        var playedEachOtherPairs = getParticipantsPlayedEachOtherPairs();

        // TODO(mr): 02.02.2019 kalkulator do obliczania wag
        var graph = new CompleteGraph<String>((a, b) -> 0., new GraphFactory<>());

        var participantsIds = getTournamentParticipantsIds();
        if (participantsIds.size() % 2 != 0) {
            participantsIds.add(null);
        }

        graph.generateGraph(participantsIds);
        graph.unlinkVertexesWithValues(playedEachOtherPairs);
        graph.unlinkVertexesWithValues(getParticipantsWithByAsNullOpponent());
        graph.makeFinalSetup();
        graph.findShortestPath();

        List<String> shortestPath = graph.getShortestPath().stream()
                .map(Vertex::getValue)
                .collect(Collectors.toList());

        List<SwissGameFixture> games = new ArrayList<>();
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

    private List<ImmutablePair<String, String>> getParticipantsWithByAsNullOpponent() {
        return tournamentSystem.getSystemState().getParticipantsWithBye().stream()
                .map(id -> ImmutablePair.of(id, (String) null))
                .collect(Collectors.toList());
    }

    private List<SwissGameFixture> createGameFixtures(List<String> shortestPath) {
        var games = new ArrayList<SwissGameFixture>();
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

    private List<ImmutablePair<String, String>> getParticipantsPlayedEachOtherPairs() {
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

    @Override
    public List<SwissGameFixture> getGeneratedGames(List<String> gamesIds) {
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
}
