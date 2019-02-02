package pl.wturnieju.tournament.system;

import pl.wturnieju.gameeditor.finish.FinishGameUpdateEvent;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.RoundRobinSystemState;

public class RoundRobinTournamentSystem extends TournamentSystem<RoundRobinSystemState> {
    public RoundRobinTournamentSystem(ISystemStateService<RoundRobinSystemState> stateService,
            Tournament tournament) {
        super(stateService, tournament);
    }

    @Override
    protected void createSystemState() {
        var state = new RoundRobinSystemState();

        initCommonSystemStateFields(state);

        stateService.insertSystemState(state);
    }

    @Override
    public void finishTournament() {
        // TODO(mr): 31.01.2019 impl
    }

    @Override
    public GameFixture finishGame(FinishGameUpdateEvent finishGameUpdateEvent) {
        return null;
    }
}
