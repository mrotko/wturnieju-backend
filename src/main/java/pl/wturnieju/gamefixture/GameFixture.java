package pl.wturnieju.gamefixture;

import lombok.Data;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.tournament.Participant;

@Data
public class GameFixture {

    private String id;

    private Timestamp startDate;

    private Timestamp endDate;

    private Timestamp finishedDate;

    private Boolean shortDate;

    private Participant homeParticipant;

    private Score homeScore;

    private Participant awayParticipant;

    private Score awayScore;

    private GameStatus gameStatus;

    private int winner;

    private Integer round;

    private Boolean bye;

    private Boolean live;
}
