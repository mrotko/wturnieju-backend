package pl.wturnieju.handler;

@Deprecated
public class RoundRobinTournamentSystem extends TournamentSystem {

    //    @Override
    //    public List<Fixture> prepareNextRound() {
    //
    //        var playersWithNotPlayedPlayers = groupPlayersWithNotPlayed();
    //        var pairedPlayers = pairPlayersRandomMethod(playersWithNotPlayedPlayers);
    //
    //        return generateFixtures(pairedPlayers);
    //    }
    //
    //    @Override
    //    protected void updateTable() {
    //
    //    }
    //
    //    private Map<String, String> pairPlayersRandomMethod(Map<String, List<String>> playersWithNotPlayedPlayers) {
    //        var availablePlayers = playersWithNotPlayedPlayers.keySet();
    //
    //        Map<String, String> pairedPlayers = new HashMap<>();
    //
    //        playersWithNotPlayedPlayers.forEach((participantId, opponentsIds) -> {
    //            opponentsIds.stream()
    //                    .filter(availablePlayers::contains)
    //                    .findAny()
    //                    .ifPresent(oId -> {
    //                        pairedPlayers.put(participantId, oId);
    //                        availablePlayers.remove(participantId);
    //                        availablePlayers.remove(oId);
    //                    });
    //
    //        });
    //
    //        if (!availablePlayers.isEmpty()) {
    //            pairedPlayers.put(availablePlayers.iterator().next(), null);
    //        }
    //
    //        return pairedPlayers;
    //    }
    //
    //
    //    @Override
    //    public void updateTournament(GenericTournamentUpdateBundle bundle) {
    //
    //    }
    //
    //    @Override
    //    public void updateFixture(GenericFixtureUpdateBundle bundle) {
    //
    //    }
}
