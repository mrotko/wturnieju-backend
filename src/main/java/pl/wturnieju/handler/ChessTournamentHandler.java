package pl.wturnieju.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.wturnieju.model.ChessTournament;
import pl.wturnieju.model.generic.TournamentState;


@RequiredArgsConstructor
@Getter
@Setter
public class ChessTournamentHandler<T extends TournamentState> extends TournamentHandler<T> {


    private final ChessTournament chessTournament;

    @Override
    public void save() {
        
    }

    @Override
    public void update() {

    }
}
