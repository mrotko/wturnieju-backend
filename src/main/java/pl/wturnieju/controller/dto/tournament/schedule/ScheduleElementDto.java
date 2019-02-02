package pl.wturnieju.controller.dto.tournament.schedule;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.controller.dto.tournament.ParticipantDto;
import pl.wturnieju.controller.dto.tournament.gamefixture.ScoreDto;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
public class ScheduleElementDto {

    private String gameId;

    private Timestamp startDate;

    private Timestamp endDate;

    private Boolean shortDate;

    private ParticipantDto homeParticipant;

    private ScoreDto homeScore;

    private ParticipantDto awayParticipant;

    private ScoreDto awayScore;

    private Boolean bye;

    private GameStatus gameStatus;

    private Integer winner;
}
