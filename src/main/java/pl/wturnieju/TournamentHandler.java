package pl.wturnieju;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.ITournamentSystem;

@Getter
@Setter
public abstract class TournamentHandler<T extends TournamentState> implements ITournamentHandler {

    protected TournamentStateRepository stateRepository;

    protected T tournamentState;

    protected ITournamentSystem tournamentSystem;
}
