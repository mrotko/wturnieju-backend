package pl.wturnieju.schedule;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.gamefixture.Score;
import pl.wturnieju.gamefixture.SwissGameFixture;
import pl.wturnieju.gamefixture.Team;
import pl.wturnieju.graph.SimpleVertex;
import pl.wturnieju.tournament.system.SwissTournamentSystem;

@RequiredArgsConstructor
public class SwissScheduleEditor extends ScheduleEditor<SwissGameFixture> {

    private final SwissTournamentSystem tournamentSystem;

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
                state.getTeamsWithBye().add(game.getHomeTeam().getId());
            } else {
                state.getTeamsPlayedEachOther().computeIfAbsent(
                        game.getHomeTeam().getId(),
                        k -> new ArrayList<>()
                ).add(game.getAwayTeam().getId());

                state.getTeamsPlayedEachOther().computeIfAbsent(
                        game.getAwayTeam().getId(),
                        k -> new ArrayList<>()
                ).add(game.getHomeTeam().getId());
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
        var playedEachOther = state.getTeamsPlayedEachOther();

        Map<String, SimpleVertex<String>> vertices = new HashMap<>();

        var idCounter = 0;
        for (var p : tournamentSystem.getTournament().getParticipants()) {
            var vertex = new SimpleVertex<>(idCounter++, p.getId());
            vertices.put(p.getId(), vertex);
        }

        List<String> addedVertices = new ArrayList<>();
        vertices.forEach((id, v) -> {
            if (!addedVertices.contains(id)) {
                vertices.forEach((oId, oV) -> {
                    if (!oId.equals(id) && !oV.hasEdge(v) && !playedEachOther.getOrDefault(id, Collections.emptyList())
                            .contains(oId)) {
                        v.addEdge(oV);
                    }
                });
                addedVertices.add(id);
            }
        });

        if (vertices.size() % 2 == 1) {
            SimpleVertex<String> byeVertex = new SimpleVertex<>(idCounter, null);
            vertices.values().forEach(v -> {
                if (!state.getTeamsWithBye().contains(v.getValue())) {
                    v.addEdge(byeVertex);
                }
            });
            vertices.put(null, byeVertex);
        }

        boolean allVerticesHasOneEdge = true;

        for (var v : vertices.values()) {
            if (v.getEdges().size() != 1) {
                allVerticesHasOneEdge = false;
                break;
            }
        }

        Deque<SimpleVertex<String>> visitedVertices = new ArrayDeque<>();
        if (allVerticesHasOneEdge) {
            vertices.values().forEach(v -> {
                if (!visitedVertices.contains(v)) {
                    visitedVertices.addLast(v);
                    visitedVertices.addLast(v.getFirstEdge());
                    v.removeEdge(v.getFirstEdge());
                }
            });
        } else {
            visitedVertices.addLast(vertices.entrySet().iterator().next().getValue());
            var treeSize = vertices.size();
            while (visitedVertices.size() < treeSize) {
                var visitedVertex = visitedVertices.getLast();

                SimpleVertex<String> v = null;

                for (var edge : visitedVertex.getNotVisitedEdges()) {
                    if (!visitedVertices.contains(edge)) {
                        v = edge;
                        break;
                    }
                }

                if (v != null) {
                    visitedVertices.getLast().markAsVisited(v);
                    visitedVertices.add(v);
                } else {
                    visitedVertices.getLast().getVisitedEdges().clear();
                    visitedVertices.removeLast();
                    if (visitedVertices.isEmpty()) {
                        break;
                    }
                }
            }
        }

        if (visitedVertices.isEmpty()) {
            return Collections.emptyList();
        }

        List<SwissGameFixture> games = new ArrayList<>();
        var it = visitedVertices.iterator();
        while (it.hasNext()) {
            var homeId = it.next().getValue();
            var awayId = it.next().getValue();
            if (homeId == null) {
                games.add(createGame(awayId, null));
            } else {
                games.add(createGame(homeId, awayId));
            }
        }
        state.setGeneratedGameFixtures(games);
        tournamentSystem.getStateService().updateSystemState(state);
        return games;
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

    private SwissGameFixture createGame(String participantId, String opponentId) {
        var game = new SwissGameFixture();

        game.setBye(opponentId == null);
        game.setId(new ObjectId().toString());
        game.setStartDate(null);
        game.setEndDate(null);
        game.setFinishedDate(null);
        game.setShortDate(null);
        game.setHomeTeam(createTeam(participantId));
        game.setHomeScore(createScore());
        game.setAwayTeam(createTeam(opponentId));
        game.setAwayScore(createScore());
        game.setGameStatus(GameStatus.BEFORE_START);
        game.setWinner(0);
        game.setRound(tournamentSystem.getTournament().getCurrentRound() + 1);

        return game;
    }

    private Score createScore() {
        var score = new Score();

        score.setCurrent(0.0D);
        score.setPeriods(Collections.emptyMap());

        return score;
    }

    private Team createTeam(String participantId) {
        if (participantId == null) {
            return null;
        }
        var participant = tournamentSystem.getTournament().getParticipants().stream()
                .filter(p -> p.getId().equals(participantId))
                .findFirst().orElse(null);

        if (participant == null) {
            return null;
        }

        var team = new Team();
        team.setId(participantId);
        team.setMembersIds(Collections.singletonList(participantId));
        team.setName(participant.getName());

        return team;
    }
}
