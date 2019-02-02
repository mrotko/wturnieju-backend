package pl.wturnieju.gamefixture;

import pl.wturnieju.tournament.Participant;

public interface GameFixtureFactory<T extends GameFixture> {

    T createGameFixture(Participant participant, Participant opponent);
}
