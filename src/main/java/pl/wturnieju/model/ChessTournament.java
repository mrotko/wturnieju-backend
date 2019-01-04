package pl.wturnieju.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.wturnieju.model.generic.Tournament;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChessTournament extends Tournament {

    public ChessTournament() {
        competitionType = CompetitionType.CHESS;
    }
}
