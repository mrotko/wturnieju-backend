package pl.wturnieju.controller.dto.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import pl.wturnieju.controller.dto.config.TableColumnType;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.ParticipantType;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentSystemType;
import pl.wturnieju.tournament.GameResultType;
import pl.wturnieju.tournament.StageType;
import pl.wturnieju.tournament.TournamentStatus;

@Data
public class TournamentDto {

    private String id;

    private String name;

    private String description;

    private String place;

    private String img;

    private TournamentStatus status;

    private AccessOption accessOption;

    private List<ParticipantDto> participants = new ArrayList<>();

    private IProfile owner;

    private Timestamp startDate;

    private Timestamp endDate;

    private TournamentSystemType systemType;

    private CompetitionType competitionType;

    private ParticipantType participantType;

    private Integer minParticipants;

    private Integer maxParticipants;

    private Integer plannedRounds;

    private String invitationToken;

    private Map<GameResultType, Double> scoring;

    private Integer currentRound;

    private StageType currentStageType;

    private List<StageType> stageTypes;

    private List<TableColumnType> tableColumns;

    private List<GroupDto> groups;

    private List<StageType> requiredAllGamesEndedStageTypes;

    private Integer gamePeriodsNumber;
}
