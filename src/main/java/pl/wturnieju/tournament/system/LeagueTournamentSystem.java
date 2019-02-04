package pl.wturnieju.tournament.system;

import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SystemState;

public class LeagueTournamentSystem extends TournamentSystem {
    public LeagueTournamentSystem(ISystemStateService stateService,
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

    }

    @Override
    public void startNextTournamentStage() {
        var state = getSystemState();

        state.getGeneratedGameFixtures().clear();

        getStateService().updateSystemState(state);
    }
}
