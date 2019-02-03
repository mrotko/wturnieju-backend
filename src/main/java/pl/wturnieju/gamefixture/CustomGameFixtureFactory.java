package pl.wturnieju.gamefixture;

import pl.wturnieju.tournament.Participant;

public class CustomGameFixtureFactory extends GameFixtureFactory<CustomGameFixture> {

    @Override
    public CustomGameFixture createGameFixture(Participant participant, Participant opponent) {
        var game = new CustomGameFixture();

        initGameFixture(game, participant, opponent);

        return game;
    }
}
