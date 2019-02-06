package pl.wturnieju.config.tournament.creator;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.controller.dto.config.TableColumnType;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.ParticipantType;
import pl.wturnieju.model.TournamentSystemType;
import pl.wturnieju.tournament.StageType;

@Getter
@Setter
public class TournamentCreatorConfigurationData {

    private List<AccessOption> accessOptions = Arrays.asList(AccessOption.values());

    private List<CompetitionType> competitionTypes = Arrays.asList(CompetitionType.values());

    private Map<CompetitionType, List<TournamentSystemType>> systemTypes = new EnumMap<>(CompetitionType.class);

    private Map<CompetitionType, List<ParticipantType>> participantTypes = new EnumMap<>(
            CompetitionType.class);

    private Map<TournamentSystemType, List<TableColumnType>> columnTypes = new EnumMap<>(
            TournamentSystemType.class);

    private Map<TournamentSystemType, List<StageType>> stageTypes = new EnumMap<>(
            TournamentSystemType.class);

    private Map<TournamentSystemType, List<StageType>> requiredAllGamesEndedStageTypesMapping;

    private Map<CompetitionType, List<Integer>> competitionTypeToGamePeriodsNumberMapping;
}
