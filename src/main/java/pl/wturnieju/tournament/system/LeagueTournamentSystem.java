package pl.wturnieju.tournament.system;

import org.apache.commons.lang3.tuple.ImmutablePair;

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

    @Override
    public void startNextTournamentStage() {
        var state = getSystemState();

        state.getParticipantsPlayedEachOther().clear();
        state.getGeneratedGameFixtures().clear();
        state.getParticipantsWithBye().clear();

        state.getGameFixtures().forEach(game -> state.getPairsAfterFirstRound().add(
                ImmutablePair.of(game.getHomeParticipantId(), game.getAwayParticipantId())));

        getStateService().updateSystemState(state);
    }
}
