package pl.wturnieju.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.wturnieju.controller.dto.config.TableColumnType;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.ParticipantType;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentSystemType;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Tournament extends Persistent {

    private String name;

    private String description;

    private String place;

    private String img;

    private TournamentStatus status;

    private AccessOption accessOption;

    private String invitationToken;

    private List<String> participantIds = new ArrayList<>();

    private IProfile owner;

    private Timestamp startDate;

    private Timestamp endDate;

    private TournamentSystemType systemType;

    private CompetitionType competitionType;

    private ParticipantType participantType;

    private int currentRound;

    private LegType currentLegType;

    private StageType currentStageType;

    private List<StageType> stageTypes;

    private Requirements requirements;

    private Map<GameResultType, Double> scoring;

    private List<TableColumnType> tableColumns = new ArrayList<>();

    private List<String> groupIds;

    private Integer gamePeriodsNumber;
}
