package pl.wturnieju.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.wturnieju.model.generic.Tournament;
import pl.wturnieju.model.generic.TournamentSystemState;


@Getter
@Setter
@RequiredArgsConstructor
public abstract class TournamentSystem<T extends TournamentSystemState> implements
        ITournamentSystem {

    protected Tournament tournament;

    protected T state;
}
