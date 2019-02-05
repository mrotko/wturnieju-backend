package pl.wturnieju.tournament.system;

import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.service.ITournamentTableService;
import pl.wturnieju.tournament.Tournament;

public class LeagueTournamentSystem extends TournamentSystem {

    private final ITournamentTableService tournamentTableService;

    public LeagueTournamentSystem(
            ITournamentTableService tournamentTableService,
            IGameFixtureService gameFixtureService,
            IGroupService groupService,
            IParticipantService participantsService,
            Tournament tournament) {
        super(gameFixtureService, groupService, participantsService, tournament);
        this.tournamentTableService = tournamentTableService;
    }

    @Override
    public void finishTournament() {

    }

    @Override
    public void startNextTournamentStage() {

    }

    @Override
    public void startTournament() {
        prepareParticipantsBeforeStart();
        var group = createLeagueGroup();
        groupService.insert(group);
        tournament.getGroupIds().add(group.getId());
    }

    @Override
    protected int calculatePlannedRounds() {
        return 2 * (tournament.getParticipantIds().size() - 1);
    }
}
