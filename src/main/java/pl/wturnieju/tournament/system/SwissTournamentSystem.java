package pl.wturnieju.tournament.system;

import java.util.Collections;

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
    protected void createSystemState() {
        var state = new SwissSystemState();

        state.setTeamsWithBye(Collections.emptyList());
        state.setGameFixtures(Collections.emptyList());
        state.setTeamsPlayedEachOther(Collections.emptyMap());

        stateService.insertSystemState(state);
    }
}
