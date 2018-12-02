package pl.wturnieju.handler;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentSystemState;


@Getter
@Setter
@RequiredArgsConstructor
public abstract class TournamentSystem<T extends TournamentSystemState> implements
        ITournamentSystem {

    protected Tournament tournament;

    protected T state;

    public abstract List<Fixture> prepareNextRound();

    public abstract void createNextRoundFixtures(List<Fixture> fixtures);
}
