package pl.wturnieju.model;

import lombok.Data;
import lombok.ToString;
import pl.wturnieju.model.generic.Tournament;

@Data
@ToString(callSuper = true)
public class ChessTournament extends Tournament {

    public ChessTournament() {
        competitionType = CompetitionType.CHESS;
    }
}
