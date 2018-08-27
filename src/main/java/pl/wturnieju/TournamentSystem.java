package pl.wturnieju;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.wturnieju.model.ITournamentSystem;


@Getter
@Setter
@RequiredArgsConstructor
public abstract class TournamentSystem<T extends TournamentState> implements ITournamentSystem {

    protected T state;
}
