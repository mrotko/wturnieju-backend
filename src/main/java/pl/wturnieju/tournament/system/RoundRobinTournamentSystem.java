package pl.wturnieju.tournament.system;

import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SystemState;

public class RoundRobinTournamentSystem extends TournamentSystem {
    public RoundRobinTournamentSystem(ISystemStateService stateService,
            Tournament tournament) {
        super(stateService, tournament);
    }

    @Override
    protected void createSystemState() {
        var state = new SystemState();

        initCommonSystemStateFields(state);

        stateService.insertSystemState(state);
    }

    @Override
    public void finishTournament() {
        // TODO(mr): 31.01.2019 impl
    }

    @Override
    public void startNextTournamentStage() {

    }
}
