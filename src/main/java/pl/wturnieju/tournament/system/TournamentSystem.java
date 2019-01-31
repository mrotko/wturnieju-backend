package pl.wturnieju.tournament.system;

import java.util.Collections;

import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gameeditor.start.StartGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.ParticipantStatus;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SystemState;
import pl.wturnieju.tournament.system.table.TournamentTable;

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
        state.setTeamsWithBye(Collections.emptyList());
        state.setGameFixtures(Collections.emptyList());
        state.setTeamsPlayedEachOther(Collections.emptyMap());
    }

    protected abstract void createSystemState();

    public abstract void finishTournament();

    public abstract TournamentTable buildTournamentTable();

    public abstract GameFixture startGame(StartGameUpdateEvent startGameUpdateEvent);

    public abstract GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent);


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
