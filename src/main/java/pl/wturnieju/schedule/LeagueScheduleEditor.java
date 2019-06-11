package pl.wturnieju.schedule;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGeneratedGamesService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.LegType;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.Group;

public class LeagueScheduleEditor extends ScheduleEditor {


    public LeagueScheduleEditor(IParticipantService participantService,
            IGeneratedGamesService generatedGamesService,
            IGameFixtureService gameFixtureService,
            IGroupService groupService, Tournament tournament) {
        super(participantService, generatedGamesService, gameFixtureService, groupService, tournament);
    }

    @Override
    protected BiFunction<String, String, Double> getWeightCalculationMethod() {
        return (a, b) -> 0.;
    }

    @Override
    protected List<GameFixture> createGameFixtures(List<String> shortestPath, Group group) {
        var games = super.createGameFixtures(shortestPath, group);
        var firstLegPairsIds = getFirstLegPairsIds(group.getId());

        games.forEach(game -> {
            var homeId = game.getHomeParticipantId();
            var awayId = game.getAwayParticipantId();
            if (firstLegPairsIds.contains(ImmutablePair.of(homeId, awayId))) {
                swapParticipants(game);
            }
            game.setPreviousGameFixtureId(findFirstLegGameId(game));
        });

        return games;
    }

    private String findFirstLegGameId(GameFixture gameFixture) {
        var previousGame = gameFixtureService.findLastParticipantsGame(
                gameFixture.getHomeParticipantId(),
                gameFixture.getAwayParticipantId());
        return previousGame.map(Persistent::getId).orElse(null);
    }

    private List<ImmutablePair<String, String>> getFirstLegPairsIds(String groupId) {
        return gameFixtureService.getAllByGroupIdAndLegType(groupId, LegType.FIRST).stream()
                .map(game -> ImmutablePair.of(game.getHomeParticipantId(), game.getAwayParticipantId()))
                .collect(Collectors.toList());
    }
}
