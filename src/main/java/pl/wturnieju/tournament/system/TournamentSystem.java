package pl.wturnieju.tournament.system;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ComparisonChain;

import pl.wturnieju.gameeditor.GameEditorFactory;
import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.GameResultType;
import pl.wturnieju.tournament.ParticipantStatus;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SystemState;
import pl.wturnieju.tournament.system.state.SystemStateManager;
import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableGeneratorBuilder;

public abstract class TournamentSystem {

    protected final ISystemStateService stateService;

    private final Tournament tournament;

    public TournamentSystem(ISystemStateService stateService,
            Tournament tournament) {
        this.stateService = stateService;
        this.tournament = tournament;
    }

    protected void prepareParticipantsBeforeStart() {
        var participants = tournament.getParticipants();
        participants.removeIf(p -> p.getInvitationStatus() != InvitationStatus.ACCEPTED);
        participants.forEach(p -> p.setParticipantStatus(ParticipantStatus.ACTIVE));
    }

    public void startTournament() {
        prepareParticipantsBeforeStart();
        createSystemState();
    }

    protected void initCommonSystemStateFields(SystemState state) {
        state.setTournamentId(getTournament().getId());
    }

    protected abstract void createSystemState();

    public abstract void finishTournament();

    public abstract void startNextTournamentStage();

    public TournamentTable buildTournamentTable() {
        var tableGenerator = TournamentTableGeneratorBuilder.builder()
                .withGames(getStateManager().getLeagueStageEndedGames())
                .withParticipants(getTournament().getParticipants())
                .withPointsForWin(getPoints(GameResultType.WIN))
                .withPointsForDraw(getPoints(GameResultType.DRAW))
                .withPointsForLose(getPoints(GameResultType.LOSE))
                .withRowComparator(((o1, o2) -> ComparisonChain.start()
                        .compare(o2.getPoints(), o1.getPoints())
                        .compare(o2.getSmallPoints(), o2.getSmallPoints())
                        .result()))
                .build();

        return tableGenerator.generateTable();
    }

    public List<TournamentTable> buildGroupsTables() {
        List<TournamentTable> groupTables = new ArrayList<>();

        getStateManager().getGroupStageEndedGames().forEach((key, value) -> {
            var tableGenerator = TournamentTableGeneratorBuilder.builder()
                    .withGames(value)
                    .withParticipants(key.getParticipants())
                    .withPointsForWin(getPoints(GameResultType.WIN))
                    .withPointsForDraw(getPoints(GameResultType.DRAW))
                    .withPointsForLose(getPoints(GameResultType.LOSE))
                    .withRowComparator(((o1, o2) -> ComparisonChain.start()
                            .compare(o2.getPoints(), o1.getPoints())
                            .compare(o2.getSmallPoints(), o2.getSmallPoints())
                            .result()))
                    .build();

            groupTables.add(tableGenerator.generateTable());
        });

        return groupTables;
    }

    private Double getPoints(GameResultType gameResultType) {
        return tournament.getScoring().getOrDefault(gameResultType, 0.);
    }

    public GameFixture startGame(StartGameUpdateEvent startGameUpdateEvent) {
        var state = getSystemState();
        var manager = new SystemStateManager(state);

        var factory = new GameEditorFactory(getTournament().getCompetitionType());
        var editor = factory.createGameEditor(manager.getGameById(startGameUpdateEvent.getGameId()));
        var game = editor.startGame(startGameUpdateEvent);
        stateService.updateSystemState(state);

        return game;
    }

    protected SystemStateManager getStateManager() {
        return new SystemStateManager(getSystemState());
    }

    public GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent) {
        var state = getSystemState();
        var manager = new SystemStateManager(state);

        var factory = new GameEditorFactory(getTournament().getCompetitionType());
        var editor = factory.createGameEditor(manager.getGameById(finishGameUpdateEvent.getGameId()));
        var game = editor.finishGame(finishGameUpdateEvent);
        stateService.updateSystemState(state);

        return game;
    }

    public ISystemStateService getStateService() {
        return stateService;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public SystemState getSystemState() {
        return stateService.getSystemStateByTournamentId(tournament.getId());
    }
}
