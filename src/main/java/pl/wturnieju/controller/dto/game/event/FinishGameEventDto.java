package pl.wturnieju.controller.dto.game.event;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.controller.dto.tournament.gamefixture.ScoreDto;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
public class FinishGameEventDto extends GameEventDto {

    private Timestamp finishedDate;

    private ScoreDto homeScore;

    private ScoreDto awayScore;

    private Integer winner;
}
