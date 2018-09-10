package pl.wturnieju.model;

import lombok.Data;
import pl.wturnieju.model.generic.Tournament;

@Data
public class ChessTournament extends Tournament {

    private int moveTimeSec;

    private int incTimeSec;

    public ChessTournament() {
        competitionType = CompetitionType.CHESS;
    }


}
