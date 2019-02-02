package pl.wturnieju.tournament.system;

import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.KnockOutSystemState;

public class KnockOutTournamentSystem extends TournamentSystem<KnockOutSystemState> {
    public KnockOutTournamentSystem(ISystemStateService<KnockOutSystemState> stateService,
            Tournament tournament) {
        super(stateService, tournament);
    }

    @Override
    protected void createSystemState() {
        var state = new KnockOutSystemState();

        initCommonSystemStateFields(state);

        stateService.insertSystemState(state);
    }

    @Override
    public void finishTournament() {

    }
}
