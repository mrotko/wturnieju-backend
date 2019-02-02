package pl.wturnieju.tournament.system;

import pl.wturnieju.service.ISystemStateService;
import pl.wturnieju.tournament.Tournament;
import pl.wturnieju.tournament.system.state.LeagueSystemState;

public class LeagueTournamentSystem extends TournamentSystem<LeagueSystemState> {
    public LeagueTournamentSystem(ISystemStateService<LeagueSystemState> stateService,
            Tournament tournament) {
        super(stateService, tournament);
    }

    @Override
    protected void createSystemState() {
        var state = new LeagueSystemState();

        initCommonSystemStateFields(state);

        stateService.insertSystemState(state);
    }

    @Override
    public void finishTournament() {

    }
}
