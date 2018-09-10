package pl.wturnieju.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pl.wturnieju.model.Duel;
import pl.wturnieju.model.swiss.SwissState;
import pl.wturnieju.model.swiss.SwissSystemParticipant;

public class SwissTournamentSystem extends TournamentSystem<SwissState> {

    private int declaredRounds;

    private Map<Double, List<SwissSystemParticipant>> pointsToPlayersGroup = new HashMap<>();

    @Override
    public void drawNextRound() {
        groupPlayersByPoints();
        int nextRound = getState().getCurrentRound() + 1;

        getState().getRoundToDuelsMap().put(nextRound, generateRandomDuels());
    }

    private List<Duel> generateRandomDuels() {
        List<SwissSystemParticipant> participants = new ArrayList<>(getState().getParticipants());
        Collections.shuffle(participants, new Random(System.currentTimeMillis()));
        List<Duel> duels = new ArrayList<>();

        int pivot = participants.size() / 2;

        for (int i = 0; i < pivot; i++) {
            duels.add(getState().getTournament().getDuelBuilder().builder()
                    .withFirstPlayer(participants.get(i).getProfile())
                    .withSecondPlayer(participants.get(i + pivot).getProfile())
                    .build()
            );
        }
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
