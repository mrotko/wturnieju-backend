package pl.wturnieju.schedule;

import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGeneratedGamesService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.service.ITournamentTableService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.Group;
import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableRow;


public class SwissScheduleEditor extends ScheduleEditor {

    private static final Integer HOME_SIDE = 1;

    private static final Integer AWAY_SIDE = 2;

    private static final Integer MAX_SIDE_STREAK = 3;

    private static final Integer MAX_SCHEDULE_GENERATION_ATTEMPTS = 100;

    private final ITournamentTableService tournamentTableService;

    private List<TournamentTableRow> tableRows;

    private Map<String, TournamentTableRow> participantIdToRowMapping;

    private Map<ImmutablePair<String, Integer>, Integer> participantIdAndSideToSideStreakMapping;

    public SwissScheduleEditor(
            ITournamentTableService tournamentTableService,
            IParticipantService participantService,
            IGeneratedGamesService generatedGamesService,
            IGameFixtureService gameFixtureService,
            IGroupService groupService, Tournament tournament) {
        super(participantService, generatedGamesService, gameFixtureService, groupService, tournament);
        this.tournamentTableService = tournamentTableService;
    }

    @Override
    protected BiFunction<String, String, Double> getWeightCalculationMethod() {
        return this::calculatePointsDifferenceBetweenParticipants;
    }

    private double calculatePointsDifferenceBetweenParticipants(String homeParticipantId, String awayParticipantId) {
        return Math.abs(getPointsByParticipantId(homeParticipantId) - getPointsByParticipantId(awayParticipantId));
    }

    private Double getPointsByParticipantId(String id) {
        if (id != null) {
            return participantIdToRowMapping.get(id).getPoints();
        }
        return 0.;
    }

    @Override
    protected List<GameFixture> generateGamesWithoutSaving(String groupId) {
        scheduleGenerationAttempts++;
        initGroupData(groupId);
        return super.generateGamesWithoutSaving(groupId);
    }

    private void initGroupData(String groupId) {
        initParticipantIdToRowMapping(groupId);
        initParticipantIdAndSideToSideStrikeMapping(groupId);
    }

    private void initParticipantIdAndSideToSideStrikeMapping(String groupId) {
        var gameFixtures = gameFixtureService.getAllByGroupId(groupId);
        participantIdAndSideToSideStreakMapping = new HashMap<>();
        gameFixtures.stream()
                .filter(game -> !game.getBye())
                .forEach(game -> {
                    var homeId = game.getHomeParticipantId();
                    addSideStreakCounterMap(homeId, HOME_SIDE, AWAY_SIDE);

                    var awayId = game.getAwayParticipantId();
                    addSideStreakCounterMap(awayId, AWAY_SIDE, HOME_SIDE);
                });
    }

    private void addSideStreakCounterMap(String participantId, Integer sideToIncrease, Integer sideToReset) {
        participantIdAndSideToSideStreakMapping.putIfAbsent(ImmutablePair.of(participantId, sideToIncrease), 0);
        participantIdAndSideToSideStreakMapping.computeIfPresent(ImmutablePair.of(participantId, sideToIncrease),
                (key, counter) -> counter++);
        participantIdAndSideToSideStreakMapping.put(ImmutablePair.of(participantId, sideToReset), 0);
    }

    private void initParticipantIdToRowMapping(String groupId) {
        participantIdToRowMapping = ofNullable(tournamentTableService.getByGroupId(groupId))
                .map(TournamentTable::getRows)
                .orElse(Collections.emptyList()).stream()
                .collect(Collectors.toMap(TournamentTableRow::getTeamId, row -> row));
    }

    @Override
    protected List<GameFixture> createGameFixtures(List<String> shortestPath, Group group) {
        var games = super.createGameFixtures(shortestPath, group);

        resolveSameSideStreakProblem(games);
        addSmallPointsToGames(games);

        return games;
    }

    private void resolveSameSideStreakProblem(List<GameFixture> games) {
        games.stream()
                .filter(game -> !game.getBye())
                .forEach(game -> {
                    var home = game.getHomeParticipant();
                    var homeStreakHome = getSideStreak(home.getId(), HOME_SIDE);

                    var away = game.getAwayParticipant();
                    var awayStreakAway = getSideStreak(away.getId(), AWAY_SIDE);

                    if (awayStreakAway > 0 && homeStreakHome > 0) {
                        swapHomeAwayParticipants(game);
                    } else if (Math.abs(homeStreakHome - awayStreakAway) > 1) {
                        swapHomeAwayParticipants(game);
                    }
                });
    }

    private void addSmallPointsToGames(List<GameFixture> games) {
        games.forEach(game -> {
            game.setHomeSmallPoints(getPointsByParticipantId(game.getHomeParticipantId()));
            game.setAwaySmallPoints(getPointsByParticipantId(game.getAwayParticipantId()));
        });
    }

    private Integer getSideStreak(String participantId, Integer side) {
        return participantIdAndSideToSideStreakMapping.getOrDefault(ImmutablePair.of(participantId, side), 0);
    }

    @Override
    protected List<String> createParticipantsPairsPath(List<String> participantsIds) {
        LinkedList<String> participantsPairsPath = new LinkedList<>();
        List<String> participantsWithoutPairs = new ArrayList<>();
        boolean containsNull = participantsIds.contains(null);
        participantsIds.remove(null);

        var pointsToParticipantsIdsMapping = groupParticipantsByPoints(participantsIds);
        pointsToParticipantsIdsMapping.forEach((points, ids) -> {
            if (ids.size() % 2 == 0) {
                var generatedPath = super.createParticipantsPairsPath(ids);
                if (generatedPath.isEmpty()) {
                    participantsWithoutPairs.addAll(ids);
                } else {
                    participantsPairsPath.addAll(generatedPath);
                }
            } else {
                int removedItemIndex = 0;
                while (removedItemIndex < ids.size()) {
                    var idsWithoutOne = new ArrayList<>(ids);
                    idsWithoutOne.remove(removedItemIndex);
                    var generatedPath = super.createParticipantsPairsPath(idsWithoutOne);
                    if (!generatedPath.isEmpty()) {
                        participantsWithoutPairs.add(ids.get(removedItemIndex));
                        participantsPairsPath.addAll(generatedPath);
                        break;
                    }
                    removedItemIndex++;
                }

                if (removedItemIndex >= ids.size()) {
                    participantsWithoutPairs.addAll(ids);
                }
            }
        });
        if (containsNull) {
            participantsWithoutPairs.add(null);
        }

        participantsPairsPath.addAll(super.createParticipantsPairsPath(participantsWithoutPairs));
        return participantsPairsPath;
    }

    Map<Double, List<String>> groupParticipantsByPoints(List<String> participantsIds) {
        return participantsIds.stream()
                .collect(Collectors.groupingBy(this::getPointsByParticipantId, Collectors.toList()));
    }
}

