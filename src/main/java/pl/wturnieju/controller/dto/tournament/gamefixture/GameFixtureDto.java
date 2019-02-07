package pl.wturnieju.controller.dto.tournament.gamefixture;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.PeriodsConfig;
import pl.wturnieju.controller.dto.tournament.ParticipantDto;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.tournament.LegType;
import pl.wturnieju.tournament.StageType;

@Getter
@Setter
public class GameFixtureDto {

    private String id;

    private String groupId;

    private String tournamentId;

    private String previousGameFixtureId;

    private Timestamp startDate;

    private Timestamp endDate;

    private Timestamp finishedDate;

    private ParticipantDto homeParticipant;

    private ScoreDto homeScore;

    private ParticipantDto awayParticipant;

    private ScoreDto awayScore;

    private GameStatus gameStatus;

    private int winner;

    private Integer round;

    private Boolean bye;

    private Boolean live;

    private LegType legType;

    private StageType stageType;

    private AccessOption accessOption;

    private CompetitionType competitionType;

    private Double homeSmallPoints;

    private Double awaySmallPoints;

    private PeriodsConfig periodsConfig;
}
