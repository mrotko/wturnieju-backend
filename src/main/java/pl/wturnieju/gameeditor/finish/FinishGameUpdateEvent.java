package pl.wturnieju.gameeditor.finish;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.gameeditor.GameUpdateEvent;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
public abstract class FinishGameUpdateEvent extends GameUpdateEvent {

    private Timestamp finishDate;

    private Integer winner;
}
