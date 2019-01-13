package pl.wturnieju.controller.dto.gamefixture;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.controller.dto.ScoreDto;

@Getter
@Setter
public class UpdateGameResult {

    private String gameId;

    private ScoreDto homeScore;

    private ScoreDto awayScore;
}
