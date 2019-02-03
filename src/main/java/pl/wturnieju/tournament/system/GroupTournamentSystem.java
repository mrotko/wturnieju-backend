package pl.wturnieju.tournament.system;

import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.GroupSystemState;

public class GroupTournamentSystem extends TournamentSystem<GroupSystemState> {
    public GroupTournamentSystem(ISystemStateService<GroupSystemState> stateService,
            Tournament tournament) {
        super(stateService, tournament);
    }

    @Override
    protected void createSystemState() {
        var state = new GroupSystemState();

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
