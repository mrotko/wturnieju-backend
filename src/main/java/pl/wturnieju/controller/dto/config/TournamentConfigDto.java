package pl.wturnieju.controller.dto.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.ParticipantType;
import pl.wturnieju.model.TournamentSystemType;

@Getter
public class TournamentConfigDto {

    private List<AccessOption> accessOptions = Arrays.asList(AccessOption.values());

    private List<CompetitionType> competitionTypes = Arrays.asList(CompetitionType.values());

    private Map<CompetitionType, List<TournamentSystemType>> systemTypes = new EnumMap<>(CompetitionType.class);

    private Map<CompetitionType, List<ParticipantType>> participantTypes = new EnumMap<>(
            CompetitionType.class);

    private Map<CompetitionType, List<ColumnType>> tableColumnsTypes = new EnumMap<>(CompetitionType.class);

    public TournamentConfigDto() {
        initAvailableSystems();
        initParticipantTypes();
        initTableColumnsTypes();
    }

    private void initTableColumnsTypes() {
        tableColumnsTypes.put(CompetitionType.CHESS, Arrays.asList(
                ColumnType.LP,
                ColumnType.NAME,
                ColumnType.WINS,
                ColumnType.TOTAL_GAMES,
                ColumnType.DRAWS,
                ColumnType.LOSES,
                ColumnType.POINTS
        ));
    }

    private void initParticipantTypes() {
        participantTypes.put(CompetitionType.CHESS, Collections.singletonList(
                ParticipantType.SINGLE
        ));

        participantTypes.put(CompetitionType.FOOTBALL, Arrays.asList(
                ParticipantType.SINGLE,
                ParticipantType.TEAM
        ));

        participantTypes.put(CompetitionType.TENNIS, Arrays.asList(
                ParticipantType.SINGLE,
                ParticipantType.TEAM
        ));

        participantTypes.put(CompetitionType.CUSTOM, Arrays.asList(
                ParticipantType.SINGLE,
                ParticipantType.TEAM
        ));
    }

    private void initAvailableSystems() {
        systemTypes.put(CompetitionType.CHESS, Collections.singletonList(
                TournamentSystemType.SWISS
        ));

        systemTypes.put(CompetitionType.TENNIS, Arrays.asList(
                TournamentSystemType.LEAGUE,
                TournamentSystemType.KNOCKOUT,
                TournamentSystemType.GROUP,
                TournamentSystemType.ROUND_ROBIN
        ));

        systemTypes.put(CompetitionType.FOOTBALL, Arrays.asList(
                TournamentSystemType.LEAGUE,
                TournamentSystemType.KNOCKOUT,
                TournamentSystemType.GROUP,
                TournamentSystemType.ROUND_ROBIN
        ));

        systemTypes.put(CompetitionType.CUSTOM, Arrays.asList(
                TournamentSystemType.LEAGUE,
                TournamentSystemType.KNOCKOUT,
                TournamentSystemType.GROUP,
                TournamentSystemType.ROUND_ROBIN,
                TournamentSystemType.SWISS
        ));
    }
}
