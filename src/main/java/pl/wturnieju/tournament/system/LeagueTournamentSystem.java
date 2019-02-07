package pl.wturnieju.tournament.system;

import java.util.HashMap;
import java.util.Map;

import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.service.ITournamentTableService;
import pl.wturnieju.tournament.LegType;
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
        super.startTournament();
        prepareSingleGroupTournament();
    }

    @Override
    protected Map<Integer, LegType> createRoundToLegMapping() {
        Map<Integer, LegType> roundToLegMapping = new HashMap<>();
        var plannedRounds = tournament.getRequirements().getPlannedRounds();

        var legMask = plannedRounds / 2;

        for (int i = 0; i < plannedRounds; i++) {
            roundToLegMapping.put(i, LegType.values()[i / legMask]);
        }

        return roundToLegMapping;
    }

    @Override
    protected int calculatePlannedRounds() {
        return 2 * (tournament.getParticipantIds().size() - 1);
    }
}
