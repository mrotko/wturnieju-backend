package pl.wturnieju.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.swiss.SwissSystemParticipant;
import pl.wturnieju.model.swiss.SwissSystemState;

public class SwissTournamentSystem extends TournamentSystem<SwissSystemState> {

    private Map<Double, List<SwissSystemParticipant>> pointsToPlayersGroup = new HashMap<>();

    @Override
    public void drawNextRound() {
        groupPlayersByPoints();
        int nextRound = getState().getCurrentRoundNumber() + 1;

        getState().getRoundToFixturesMap().put(nextRound, generateRandomDuels());
    }

    private List<Fixture> generateRandomDuels() {
        List<SwissSystemParticipant> participants = new ArrayList<>(getState().getParticipants());
        Collections.shuffle(participants, new Random(System.currentTimeMillis()));
        List<Fixture> duels = new ArrayList<>();

        int pivot = participants.size() / 2;

        //        for (int i = 0; i < pivot; i++) {
        //            duels.add(getState().get().getDuelBuilder().builder()
        //                    .withHomePlayer(participants.get(i).getProfile())
        //                    .withAwayPlayer(participants.get(i + pivot).getProfile())
        //                    .build()
        //            );
        //        }
        if (participants.size() % 2 == 1) {
            participants.get(participants.size() - 1).setBye(true);
        }
        return duels;
    }

    private void groupPlayersByPoints() {
        pointsToPlayersGroup.clear();
        state.getParticipants().forEach(swissSystemParticipant -> {
            pointsToPlayersGroup.putIfAbsent(swissSystemParticipant.getPoints(), new ArrayList<>());
            pointsToPlayersGroup.get(swissSystemParticipant.getPoints()).add(swissSystemParticipant);
        });
    }


}
