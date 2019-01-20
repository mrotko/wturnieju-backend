package pl.wturnieju.gameeditor.start;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.gameeditor.GameUpdateEvent;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
public abstract class StartGameUpdateEvent extends GameUpdateEvent {

    private Timestamp startDate;
}
