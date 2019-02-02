package pl.wturnieju.config.tournament.scoring;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.TournamentSystemType;

@Getter
@Setter
public class TournamentSystem {

    private TournamentSystemType tournamentSystemType;

    private List<GameResult> gameResults;
}
