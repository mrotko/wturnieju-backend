package pl.wturnieju.controller.dto.tournament.schedule;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.controller.dto.TeamDto;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
public class ScheduleElementDto {

    private String gameId;

    private Timestamp startDate;

    private Timestamp endDate;

    private Boolean shortDate;

    private TeamDto homeTeam;

    private TeamDto awayTeam;

    private Boolean bye;

    private GameStatus gameStatus;
}
