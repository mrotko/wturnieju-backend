package pl.wturnieju.tournament.system.state;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.tournament.StageType;

@RequiredArgsConstructor
public class SystemStateManager {

    private final SystemState state;

    public Group getGroupById(String groupId) {
        return state.getAllGroups().stream()
                .filter(group -> group.getId().equals(groupId))
                .findFirst().orElse(null);
    }

    public GameFixture getGameById(String gameId) {
        return state.getAllGames().stream()
                .filter(game -> game.getId().equals(gameId))
                .findFirst().orElse(null);
    }

    public void addGames(List<GameFixture> gameFixtures) {
        gameFixtures.forEach(game -> getGroupById(game.getGroupId()).addGame(game));
    }

    public void deleteGames(List<String> gamesIds) {
        var games = state.getAllGames().stream()
                .filter(game -> gamesIds.contains(game.getId()))
                .collect(Collectors.toList());

        games.forEach(game -> getGroupById(game.getId()).deleteGame(game));
    }

    public List<GameFixture> getLeagueStageEndedGames() {
        return state.getAllGamesByStage(StageType.LEAGUE).stream()
                .filter(game -> game.getGameStatus() == GameStatus.ENDED)
                .collect(Collectors.toList());
    }

    public Map<Group, List<GameFixture>> getGroupStageEndedGames() {
        return state.getAllGamesByStage(StageType.GROUP).stream()
                .filter(game -> game.getGameStatus() == GameStatus.ENDED)
                .collect(Collectors.groupingBy(game -> getGroupById(game.getGroupId()), Collectors.toList()));
    }

    public GameFixture updateGame(GameFixture gameFixture) {
        var currentGame = getGameById(gameFixture.getId());

        currentGame.setId(gameFixture.getId());
        currentGame.setStartDate(gameFixture.getStartDate());
        currentGame.setEndDate(gameFixture.getEndDate());
        currentGame.setFinishedDate(gameFixture.getFinishedDate());
        currentGame.setShortDate(gameFixture.getShortDate());
        currentGame.setHomeParticipant(gameFixture.getHomeParticipant());
        currentGame.setHomeScore(gameFixture.getHomeScore());
        currentGame.setAwayParticipant(gameFixture.getAwayParticipant());
        currentGame.setAwayScore(gameFixture.getAwayScore());
        currentGame.setGameStatus(gameFixture.getGameStatus());
        currentGame.setWinner(gameFixture.getWinner());
        currentGame.setRound(gameFixture.getRound());
        currentGame.setBye(gameFixture.getBye());
        currentGame.setLive(gameFixture.getLive());
        currentGame.setLegType(gameFixture.getLegType());
        currentGame.setStageType(gameFixture.getStageType());
        currentGame.setGroupId(gameFixture.getGroupId());
        currentGame.setPreviousGameFixtureId(gameFixture.getPreviousGameFixtureId());

        return currentGame;
    }
}
