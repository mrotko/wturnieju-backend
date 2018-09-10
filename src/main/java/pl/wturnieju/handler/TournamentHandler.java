package pl.wturnieju.handler;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.generic.TournamentState;
import pl.wturnieju.repository.TournamentStateRepository;

@Getter
@Setter
public abstract class TournamentHandler<T extends TournamentState> implements ITournamentHandler {

    protected TournamentStateRepository stateRepository;

    protected T tournamentState;

    protected ITournamentSystem tournamentSystem;
}
