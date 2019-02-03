package pl.wturnieju.gamefixture;

import pl.wturnieju.tournament.Participant;

public class GroupGameFixtureFactory extends GameFixtureFactory<GroupGameFixture> {
    @Override
    public GroupGameFixture createGameFixture(Participant participant, Participant opponent) {
        var game = new GroupGameFixture();

        initGameFixture(game, participant, opponent);

        return game;
    }
}
