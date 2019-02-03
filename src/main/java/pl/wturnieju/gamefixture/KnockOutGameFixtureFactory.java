package pl.wturnieju.gamefixture;

import pl.wturnieju.tournament.Participant;

public class KnockOutGameFixtureFactory extends GameFixtureFactory<KnockOutGameFixture> {
    @Override
    public KnockOutGameFixture createGameFixture(Participant participant, Participant opponent) {
        var game = new KnockOutGameFixture();

        initGameFixture(game, participant, opponent);

        return game;
    }
}
