package pl.wturnieju.gamefixture;

import pl.wturnieju.tournament.Participant;

public class RoundRobinGameFixtureFactory extends GameFixtureFactory<RoundRobinGameFixture> {

    @Override
    public RoundRobinGameFixture createGameFixture(Participant participant, Participant opponent) {
        var game = new RoundRobinGameFixture();

        initGameFixture(game, participant, opponent);

        return game;
    }
}
