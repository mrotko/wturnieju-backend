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
import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGeneratedGamesService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.Group;
import pl.wturnieju.utils.DateUtils;

@RequiredArgsConstructor
public abstract class ScheduleEditor implements IScheduleEditor {

    protected int scheduleGenerationAttempts;

    protected final IParticipantService participantService;

    protected final IGeneratedGamesService generatedGamesService;

    protected final IGameFixtureService gameFixtureService;

    protected final IGroupService groupService;

    protected final Tournament tournament;

    protected final GameFixtureFactory gameFixtureFactory = new GameFixtureFactory();

    protected List<ImmutablePair<String, String>> competitors = new ArrayList<>();

    @Override
    public GameFixture updateGame(GameFixture gameFixture) {
        updateGames(Collections.singletonList(gameFixture));
        return gameFixture;
    }

    @Override
    public List<GameFixture> updateGames(List<GameFixture> gameFixtures) {
        return gameFixtureService.updateAll(gameFixtures);
    }

    @Override
    public List<GameFixture> addGames(List<GameFixture> gameFixtures) {
        updateGamesWithBye(gameFixtures);
        return gameFixtureService.saveAll(gameFixtures);
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
        gameFixtureService.deleteAllByIds(gamesIds);
        return gamesIds;
    }

    protected void prepareCompetitors(String groupId) {
        competitors = getCompetitors(groupId);
    }

    @Override
    public List<GameFixture> generateGames(String groupId) {
        prepareCompetitors(groupId);
        return generatedGamesService.insertAll(generateGamesWithoutSaving(groupId));
    }

    protected List<GameFixture> generateGamesWithoutSaving(String groupId) {
        var group = groupService.getById(groupId);
        var participantsIds = getParticipantsIdsForGamesGeneration(group);

        if (tournament.getRequirements().getPlannedRounds() <= tournament.getCurrentRound()) {
            return Collections.emptyList();
        }
        if (group.isRequiredAllGamesEnded()) {
            if (gameFixtureService.countPendingGamesByGroupId(groupId) > 0) {
                return Collections.emptyList();
            }
        }

        List<String> participantsPairsPath = createParticipantsPairsPath(participantsIds);
        return createGameFixtures(participantsPairsPath, group);
    }

    protected List<String> createParticipantsPairsPath(List<String> participantsIds) {
        var graph = new CompleteGraph<>(getWeightCalculationMethod(), new GraphFactory<>());

        graph.generateGraph(participantsIds);
        graph.unlinkVertexesWithValues(competitors);

        graph.makeFinalSetup();
        graph.findPath();

        List<String> path = graph.getPath().stream()
                .map(Vertex::getValue)
                .collect(Collectors.toList());

        if (path.isEmpty()) {
            var edges = graph.getEdges();
            if (!edges.isEmpty() && (edges.size() == participantsIds.size() / 2)) {
                edges.forEach(edge -> {
                    path.add(edge.getFirst().getValue());
                    path.add(edge.getSecond().getValue());
                });
            }
        }

        return path;
    }

    protected List<ImmutablePair<String, String>> getCompetitors(String groupId) {
        return gameFixtureService.getAllByGroupId(groupId).stream()
                .map(game -> ImmutablePair.of(game.getHomeParticipantId(), game.getAwayParticipantId()))
                .collect(Collectors.toList());
    }

    protected List<GameFixture> createGameFixtures(List<String> shortestPath, Group group) {
        var games = new ArrayList<GameFixture>();
        for (int i = 0; i < shortestPath.size(); i += 2) {
            var homeId = shortestPath.get(i);
            var awayId = shortestPath.get(i + 1);

            var home = getParticipantById(homeId);
            var away = getParticipantById(awayId);

            if (home == null) {
                games.add(gameFixtureFactory.createGameFixture(tournament, group, away, null));
            } else {
                games.add(gameFixtureFactory.createGameFixture(tournament, group, home, away));
            }
        }

        int currentRound = tournament.getCurrentRound() + 1;
        games.forEach(game -> game.setRound(currentRound));

        return games;
    }

    private List<String> getTournamentParticipantsIds(Group group) {
        return group.getParticipantIds();
    }

    private List<String> getParticipantsIdsForGamesGeneration(Group group) {
        List<String> ids = new ArrayList<>(getTournamentParticipantsIds(group));

        if (ids.size() % 2 != 0) {
            ids.add(null);
        }
        Collections.shuffle(ids);
        return ids;
    }

    @Override
    public List<GameFixture> getGeneratedGames(List<String> gamesIds) {
        return generatedGamesService.getAllByIds(gamesIds);
    }

    @Override
    public List<String> deleteGeneratedGames(List<String> gamesIds) {
        generatedGamesService.deleteAllByIds(gamesIds);
        return gamesIds;
    }

    private Participant getParticipantById(String participantId) {
        if (participantId == null) {
            return null;
        }
        return participantService.getById(participantId);
    }

    protected abstract BiFunction<String, String, Double> getWeightCalculationMethod();
}
