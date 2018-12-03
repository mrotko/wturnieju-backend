package pl.wturnieju.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.generic.GenericFixtureUpdateBundle;
import pl.wturnieju.model.generic.RoundRobinSystemState;
import pl.wturnieju.service.GenericTournamentUpdateBundle;

public class RoundRobinTournamentSystem extends TournamentSystem<RoundRobinSystemState> {

    @Override
    public List<Fixture> prepareNextRound() {

        var playersWithNotPlayedPlayers = groupPlayersWithNotPlayed();
        var pairedPlayers = pairPlayersRandomMethod(playersWithNotPlayedPlayers);

        return generateFixtures(pairedPlayers);
    }

    @Override
    protected void updateTable() {

    }

    private Map<String, String> pairPlayersRandomMethod(Map<String, List<String>> playersWithNotPlayedPlayers) {
        var availablePlayers = playersWithNotPlayedPlayers.keySet();

        Map<String, String> pairedPlayers = new HashMap<>();

        playersWithNotPlayedPlayers.forEach((participantId, opponentsIds) -> {
            opponentsIds.stream()
                    .filter(availablePlayers::contains)
                    .findAny()
                    .ifPresent(oId -> {
                        pairedPlayers.put(participantId, oId);
                        availablePlayers.remove(participantId);
                        availablePlayers.remove(oId);
                    });

        });

        if (!availablePlayers.isEmpty()) {
            pairedPlayers.put(availablePlayers.iterator().next(), null);
        }

        return pairedPlayers;
    }


    @Override
    public void updateTournament(GenericTournamentUpdateBundle bundle) {

    }

    @Override
    public void updateFixture(GenericFixtureUpdateBundle bundle) {

    }
}
