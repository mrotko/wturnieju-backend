package pl.wturnieju.tournament.system;

import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SystemState;

public class SwissTournamentSystem extends TournamentSystem {

    public SwissTournamentSystem(ISystemStateService stateService, Tournament tournament) {
        super(stateService, tournament);
    }

    @Override
    public void finishTournament() {
        // TODO(mr): 13.01.2019 impl

    }

    @Override
    public void startNextTournamentStage() {

    }

    @Override
    protected void createSystemState() {
        var state = new SystemState();

        initCommonSystemStateFields(state);

        stateService.insertSystemState(state);
    }
}
