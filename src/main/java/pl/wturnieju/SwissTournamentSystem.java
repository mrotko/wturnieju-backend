package pl.wturnieju;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwissTournamentSystem extends TournamentSystem<SwissState> {

    private Map<Double, List<SwissSystemParticipant>> pointsToPlayersGroup = new HashMap<>();

    @Override
    public void drawNextRound() {
        groupPlayersByPoints();

    }

    private void groupPlayersByPoints() {
        pointsToPlayersGroup.clear();
        state.getParticipants().forEach(swissSystemParticipant -> {
            pointsToPlayersGroup.putIfAbsent(swissSystemParticipant.getPoints(), new ArrayList<>());
            pointsToPlayersGroup.get(swissSystemParticipant.getPoints()).add(swissSystemParticipant);
        });
    }

}
