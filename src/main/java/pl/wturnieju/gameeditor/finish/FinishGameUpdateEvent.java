package pl.wturnieju.gameeditor.finish;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.gameeditor.GameUpdateEvent;
import pl.wturnieju.gamefixture.Score;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
public class FinishGameUpdateEvent extends GameUpdateEvent {

    private Timestamp finishDate;

    private Score homeScore;

    private Score awayScore;

    private Integer winner;
}
