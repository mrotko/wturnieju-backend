package pl.wturnieju.config.tournament.scoring;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.tournament.GameResultType;

@Getter
@Setter
public class GameResult {

    private GameResultType gameResultType;

    private Double points;
}
