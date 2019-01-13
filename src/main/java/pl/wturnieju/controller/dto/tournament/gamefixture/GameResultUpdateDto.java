package pl.wturnieju.controller.dto.tournament.gamefixture;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResultUpdateDto {

    private String gameId;

    private ScoreDto homeScore;

    private ScoreDto awayScore;
}
