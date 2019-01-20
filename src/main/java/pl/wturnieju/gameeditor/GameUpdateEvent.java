package pl.wturnieju.gameeditor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class GameUpdateEvent {

    private String tournamentId;

    private String gameId;
}
