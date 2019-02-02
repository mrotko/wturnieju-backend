package pl.wturnieju.config.tournament.scoring;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.CompetitionType;

@Getter
@Setter
public class Competition {

    private CompetitionType competitionType;

    private List<TournamentSystem> tournamentSystems;
}
