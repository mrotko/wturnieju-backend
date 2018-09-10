package pl.wturnieju.model.generic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.wturnieju.model.Persistent;


@RequiredArgsConstructor
@Getter
@Setter
public abstract class TournamentState extends Persistent {

    private final Tournament tournament;

    private int currentRound;
}
