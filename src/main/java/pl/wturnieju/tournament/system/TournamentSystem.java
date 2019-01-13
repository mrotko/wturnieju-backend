package pl.wturnieju.tournament.system;

import pl.wturnieju.model.InvitationStatus;
import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.ParticipantStatus;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SystemState;

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


    protected abstract void createSystemState();

    public abstract void finishTournament();

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
