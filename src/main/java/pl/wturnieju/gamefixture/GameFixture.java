package pl.wturnieju.gamefixture;

import java.util.Optional;

import org.springframework.data.annotation.Transient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.tournament.LegType;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.StageType;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class GameFixture extends Persistent {

    private String groupId;

    private String tournamentId;

    private String previousGameFixtureId;

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

    private LegType legType;

    private StageType stageType;

    private AccessOption accessOption;

    private CompetitionType competitionType;

    @Transient
    public String getHomeParticipantId() {
        return Optional.ofNullable(homeParticipant).map(Participant::getId).orElse(null);
    }

    @Transient
    public String getAwayParticipantId() {
        return Optional.ofNullable(awayParticipant).map(Participant::getId).orElse(null);
    }
}
