package pl.wturnieju.schedule;

import java.util.function.BiFunction;

import pl.wturnieju.service.IGameFixtureService;
import pl.wturnieju.service.IGeneratedGamesService;
import pl.wturnieju.service.IGroupService;
import pl.wturnieju.service.IParticipantService;
import pl.wturnieju.tournament.Tournament;

public class GroupScheduleEditor extends ScheduleEditor {


    public GroupScheduleEditor(IParticipantService participantService,
            IGeneratedGamesService generatedGamesService,
            IGameFixtureService gameFixtureService,
            IGroupService groupService, Tournament tournament) {
        super(participantService, generatedGamesService, gameFixtureService, groupService, tournament);
    }

    @Override
    protected BiFunction<String, String, Double> getWeightCalculationMethod() {
        return (a, b) -> 0.;
    }
}
