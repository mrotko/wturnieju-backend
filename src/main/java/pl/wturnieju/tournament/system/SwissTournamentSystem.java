package pl.wturnieju.tournament.system;

import pl.wturnieju.service.impl.SwissSystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.SwissSystemState;

public class SwissTournamentSystem extends TournamentSystem<SwissSystemState> {

    public SwissTournamentSystem(SwissSystemStateService stateService, Tournament tournament) {
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
        var state = new SwissSystemState();

        initCommonSystemStateFields(state);

        stateService.insertSystemState(state);
    }
}
