package pl.wturnieju.tournament.system;

import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.Tournament;

public class LeagueTournamentSystem extends TournamentSystem {
    public LeagueTournamentSystem(IGameFixtureService gameFixtureService,
            IGroupService groupService,
            IParticipantService participantsService, Tournament tournament) {
        super(gameFixtureService, groupService, participantsService, tournament);
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
}
