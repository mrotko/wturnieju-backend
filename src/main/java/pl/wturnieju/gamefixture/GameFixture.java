package pl.wturnieju.gamefixture;

import lombok.Data;
import pl.wturnieju.model.Timestamp;

@Data
public class GameFixture {

    private String id;

    private Timestamp startDate;

    private Timestamp endDate;

    private Timestamp finishedDate;

    private Boolean shortDate;

    private Team homeTeam;

    private Score homeScore;

    private Team awayTeam;

    private Score awayScore;

    private GameStatus gameStatus;

    private int winner;

    private Integer round;

    private Boolean bye;
}
