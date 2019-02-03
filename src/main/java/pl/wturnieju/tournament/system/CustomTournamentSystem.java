package pl.wturnieju.tournament.system;

import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.CustomSystemState;

public class CustomTournamentSystem extends TournamentSystem<CustomSystemState> {

    public CustomTournamentSystem(ISystemStateService<CustomSystemState> stateService, Tournament tournament) {
        super(stateService, tournament);
    }

    @Override
    protected void createSystemState() {
        var state = new CustomSystemState();

        initCommonSystemStateFields(state);

        stateService.insertSystemState(state);
    }

    @Override
    public void finishTournament() {

    }

    @Override
    public void startNextTournamentStage() {

    }
}
