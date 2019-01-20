package pl.wturnieju.controller.dto.tournament.gamefixture;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.controller.dto.TeamDto;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.Timestamp;

@Getter
@Setter
public class GameFixtureDto {

    private String gameId;

    private Timestamp startDate;

    private Timestamp endDate;

    private Timestamp finishedDate;

    private TeamDto homeTeam;

    private ScoreDto homeScore;

    private TeamDto awayTeam;

    private ScoreDto awayScore;

    private GameStatus gameStatus;

    private int winner;

    private Integer round;

    private Boolean bye;

    private Boolean live;

    private CompetitionType competitionType;

    private String tournamentId;
}
