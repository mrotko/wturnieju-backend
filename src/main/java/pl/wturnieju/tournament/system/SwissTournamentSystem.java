package pl.wturnieju.tournament.system;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ComparisonChain;

import pl.wturnieju.gameeditor.GameEditorFactory;
import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.service.impl.SwissSystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SwissSystemState;
import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableGeneratorBuilder;

public class SwissTournamentSystem extends TournamentSystem<SwissSystemState> {

    public SwissTournamentSystem(SwissSystemStateService stateService, Tournament tournament) {
        super(stateService, tournament);
    }

    @Override
    public void finishTournament() {
        // TODO(mr): 13.01.2019 impl

    }

    @Override
    protected void createSystemState() {
        var state = new SwissSystemState();

        initCommonSystemStateFields(state);

        stateService.insertSystemState(state);
    }

    @Override
    public TournamentTable buildTournamentTable() {
        var tableGenerator = TournamentTableGeneratorBuilder.builder()
                .withGames(getEndedGames())
                .withPointsForWin(1.)
                .withPointsForDraw(0.5)
                .withPointsForLose(0.)
                .withRowComparator(((o1, o2) -> ComparisonChain.start()
                        .compare(o2.getPoints(), o1.getPoints())
                        .compare(o2.getSmallPoints(), o2.getSmallPoints())
                        .result()))
                .withParticipants(getTournament().getParticipants())
                .build();

        return tableGenerator.generateTable();
    }

    private List<GameFixture> getEndedGames() {
        return getSystemState().getGameFixtures().stream()
                .filter(game -> game.getGameStatus().equals(GameStatus.ENDED)).collect(
                        Collectors.toList());
    }

    @Override
    public GameFixture startGame(StartGameUpdateEvent startGameUpdateEvent) {
        var state = getSystemState();
        var factory = new GameEditorFactory(getTournament().getCompetitionType());
        var editor = factory.createGameEditor(getGameById(state, startGameUpdateEvent.getGameId()));
        var game = editor.startGame(startGameUpdateEvent);
        stateService.updateSystemState(state);
        return game;
    }

    private GameFixture getGameById(SwissSystemState state, String gameId) {
        return state.getGameFixtures().stream()
                .filter(game -> game.getId().equals(gameId))
                .findFirst().orElse(null);
    }

    @Override
    public GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent) {
        var state = getSystemState();
        var factory = new GameEditorFactory(getTournament().getCompetitionType());
        var editor = factory.createGameEditor(getGameById(state, finishGameUpdateEvent.getGameId()));
        var game = editor.finishGame(finishGameUpdateEvent);
        stateService.updateSystemState(state);
        return game;
    }

}
