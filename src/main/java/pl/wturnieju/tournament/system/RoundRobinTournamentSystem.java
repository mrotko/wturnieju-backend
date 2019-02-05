package pl.wturnieju.tournament.system;

import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.service.ITournamentTableService;
import pl.wturnieju.tournament.Tournament;

public class RoundRobinTournamentSystem extends TournamentSystem {

    private final ITournamentTableService tournamentTableService;

    public RoundRobinTournamentSystem(
            ITournamentTableService tournamentTableService,
            IGameFixtureService gameFixtureService,
            IGroupService groupService,
            IParticipantService participantsService,
            Tournament tournament) {
        super(gameFixtureService, groupService, participantsService, tournament);
        this.tournamentTableService = tournamentTableService;
    }

    @Override
    protected int calculatePlannedRounds() {
        return tournament.getParticipantIds().size() - 1;
    }

    @Override
    public void finishTournament() {

    }

    @Override
    public void startNextTournamentStage() {

    }

}
