package pl.wturnieju.controller.dto.game.event;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.CompetitionType;

@Getter
@Setter
public abstract class GameEventDto {

    private String tournamentId;

    private CompetitionType competitionType;

    private String gameId;

    private GameEventType gameEventType;
}
