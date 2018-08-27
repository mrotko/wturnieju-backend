package pl.wturnieju;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.wturnieju.model.ChessTournament;


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
