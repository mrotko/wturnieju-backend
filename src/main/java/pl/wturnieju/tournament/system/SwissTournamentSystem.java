package pl.wturnieju.tournament.system;

import java.util.Collections;

import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.Tournament;

public class SwissTournamentSystem extends TournamentSystem {

    public SwissTournamentSystem(IGameFixtureService gameFixtureService,
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
        tournament.setGroupIds(Collections.singletonList(group.getId()));
    }

}
