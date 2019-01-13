package pl.wturnieju.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.gamefixture.Score;
import pl.wturnieju.gamefixture.SwissGameFixture;
import pl.wturnieju.gamefixture.Team;
import pl.wturnieju.tournament.Participant;
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
        var participants = tournamentSystem.getTournament().getParticipants().stream()
                .map(Participant::getId)
                .collect(Collectors.toList());

        Map<String, List<String>> availableOpponents = participants.stream()
                .collect(Collectors.toMap(Function.identity(), id -> getAvailableOpponents(id, participants)));

        List<String> availablePlayers = new ArrayList<>();
        List<SwissGameFixture> games = new ArrayList<>();

        participants.forEach(pId -> {
            if (availablePlayers.contains(pId)) {
                var opponentId = availableOpponents.get(pId).stream()
                        .filter(availablePlayers::contains)
                        .findFirst().orElse(null);

                if (opponentId != null) {
                    availablePlayers.remove(pId);
                    availablePlayers.remove(opponentId);
                    games.add(createGame(pId, opponentId));
                }
            }
        });
        if (!availablePlayers.isEmpty()) {
            games.add(createGame(availablePlayers.get(0), null));
        }

        var state = tournamentSystem.getSystemState();
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

    private List<String> getAvailableOpponents(String participantId, List<String> opponentsIds) {
        var state = tournamentSystem.getSystemState();
        var playedList = state.getTeamsPlayedEachOther().get(participantId);
        return opponentsIds.stream()
                .filter(id -> !playedList.contains(id))
                .collect(Collectors.toList());
    }

    private SwissGameFixture createGame(String participantId, String opponentId) {
        var game = new SwissGameFixture();

        game.setBye(false);
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

        if (opponentId == null) {
            game.setBye(true);
        }

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
