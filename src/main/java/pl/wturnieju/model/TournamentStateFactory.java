package pl.wturnieju.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentSystemState;
import pl.wturnieju.model.swiss.SwissSystemParticipant;
import pl.wturnieju.model.swiss.SwissSystemState;

public class TournamentStateFactory {

    public static TournamentSystemState getInstance(Tournament tournament) {
        switch (tournament.getSystemType()) {
        case SWISS:
            return createSwissTournamentState(tournament);
        default:
            throw new IllegalArgumentException("unknown tournament system type: " + tournament.getSystemType());
        }
    }

    private static SwissSystemState createSwissTournamentState(Tournament tournament) {
        var state = new SwissSystemState();

        state.setCurrentRound(1);
        state.setLastUpdate(Timestamp.now());
        state.setParticipants(tournament.getParticipants().stream()
                .map(TournamentStateFactory::createSwissSystemParticipant)
                .collect(Collectors.toList()));
        state.setTournamentTable(TournamentTableFactory.getTable(tournament));
        state.setFixtures(new ArrayList<>());
        state.setUpdateFixtureBundles(new ArrayList<>());
        state.setUpdateTournamentBundles(new ArrayList<>());

        return state;
    }


    private static SwissSystemParticipant createSwissSystemParticipant(TournamentParticipant participant) {
        var swissParticipant = new SwissSystemParticipant();

        swissParticipant.setBye(false);
        swissParticipant.setOpponents(new LinkedList<>());
        swissParticipant.setOpponentsIdsToResultsMap(new HashMap<>());
        swissParticipant.setPoints(0.);
        swissParticipant.setProfileId(participant.getId());

        return swissParticipant;
    }

}
