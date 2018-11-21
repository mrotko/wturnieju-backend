package pl.wturnieju.handler;

import pl.wturnieju.model.generic.GenericFixtureUpdateBundle;
import pl.wturnieju.service.GenericTournamentUpdateBundle;

public interface ITournamentSystem {

    void updateTournament(GenericTournamentUpdateBundle bundle);

    void updateFixture(GenericFixtureUpdateBundle bundle);

}
