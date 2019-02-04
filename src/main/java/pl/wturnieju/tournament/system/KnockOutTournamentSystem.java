package pl.wturnieju.tournament.system;

import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.Tournament;

public class KnockOutTournamentSystem extends TournamentSystem {
    public KnockOutTournamentSystem(IGameFixtureService gameFixtureService,
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
}
