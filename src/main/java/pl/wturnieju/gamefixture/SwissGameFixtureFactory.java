package pl.wturnieju.gamefixture;

import pl.wturnieju.tournament.Participant;

public class SwissGameFixtureFactory extends GameFixtureFactory<SwissGameFixture> {

    @Override
    public SwissGameFixture createGameFixture(Participant participant, Participant opponent) {
        var game = new SwissGameFixture();

        initGameFixture(game, participant, opponent);

        return game;
    }


}
