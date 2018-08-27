package pl.wturnieju;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.generic.Tournament;


@RequiredArgsConstructor
@Getter
public abstract class TournamentState extends Persistent {

    private final Tournament tournament;
}
