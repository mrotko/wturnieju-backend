package pl.wturnieju.tournament.system;

import java.util.Collections;

import com.google.common.collect.ComparisonChain;

import pl.wturnieju.gameeditor.GameEditorFactory;
import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.ParticipantStatus;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SystemState;
import pl.wturnieju.tournament.system.table.TournamentTable;
import pl.wturnieju.tournament.system.table.TournamentTableGeneratorBuilder;

public abstract class TournamentSystem<T extends SystemState> {

    protected final ISystemStateService<T> stateService;

    private final Tournament tournament;

    public TournamentSystem(ISystemStateService<T> stateService, Tournament tournament) {
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

    protected void initCommonSystemStateFields(SystemState<? extends GameFixture> state) {
        state.setTournamentId(getTournament().getId());
        state.setParticipantsWithBye(Collections.emptyList());
        state.setGameFixtures(Collections.emptyList());
        state.setParticipantsPlayedEachOther(Collections.emptyMap());
    }

    protected abstract void createSystemState();

    public abstract void finishTournament();

    @SuppressWarnings("unchecked")
    public TournamentTable buildTournamentTable() {
        var tableGenerator = TournamentTableGeneratorBuilder.builder()
                .withGames(getSystemState().getEndedGames())
                .withParticipants(getTournament().getParticipants())
                .withPointsForWin(1.)
                .withPointsForDraw(0.5)
                .withPointsForLose(0.)
                .withRowComparator(((o1, o2) -> ComparisonChain.start()
                        .compare(o2.getPoints(), o1.getPoints())
                        .compare(o2.getSmallPoints(), o2.getSmallPoints())
                        .result()))
                .build();

        return tableGenerator.generateTable();
    }

    public GameFixture startGame(StartGameUpdateEvent startGameUpdateEvent) {
        var state = getSystemState();
        var factory = new GameEditorFactory(getTournament().getCompetitionType());
        var editor = factory.createGameEditor(getGameById(state, startGameUpdateEvent.getGameId()));
        var game = editor.startGame(startGameUpdateEvent);
        stateService.updateSystemState(state);
        return game;
    }

    protected GameFixture getGameById(SystemState<?> state, String gameId) {
        return state.getGameFixtures().stream()
                .filter(game -> game.getId().equals(gameId))
                .findFirst().orElse(null);
    }

    public GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent) {
        var state = getSystemState();
        var factory = new GameEditorFactory(getTournament().getCompetitionType());
        var editor = factory.createGameEditor(getGameById(state, finishGameUpdateEvent.getGameId()));
        var game = editor.finishGame(finishGameUpdateEvent);
        stateService.updateSystemState(state);

        return game;
    }


    public ISystemStateService<T> getStateService() {
        return stateService;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public T getSystemState() {
        return stateService.getSystemStateByTournamentId(tournament.getId());
    }
}
