package pl.wturnieju.controller.dto.tournament.creator;

import java.util.List;
import java.util.Map;

import lombok.Data;
import pl.wturnieju.controller.dto.config.TableColumnType;
import pl.wturnieju.controller.dto.tournament.PeriodsConfigDto;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.ParticipantType;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentSystemType;
import pl.wturnieju.tournament.GameResultType;
import pl.wturnieju.tournament.StageType;


@Data
public class TournamentTemplateDto {

    private String name;

    private String description;

    private String place;

    private Timestamp startDate;

    private Timestamp endDate;

    private AccessOption accessOption;

    private int maxParticipants;

    private int minParticipants;

    private CompetitionType competitionType;

    private Boolean invitationLink;

    private TournamentSystemType systemType;

    private ParticipantType participantType;

    private List<TableColumnType> tableColumns;

    private Map<GameResultType, Double> scoring;

    private List<StageType> stageTypes;

    private List<StageType> requiredAllGamesEndedStageTypes;

    private PeriodsConfigDto periodsConfig;
}
