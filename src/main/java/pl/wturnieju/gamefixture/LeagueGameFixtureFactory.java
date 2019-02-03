package pl.wturnieju.gamefixture;

import pl.wturnieju.tournament.Participant;

public class LeagueGameFixtureFactory extends GameFixtureFactory<LeagueGameFixture> {

    @Override
    public LeagueGameFixture createGameFixture(Participant participant, Participant opponent) {
        var game = new LeagueGameFixture();

        initGameFixture(game, participant, opponent);

        return game;
    }
}
