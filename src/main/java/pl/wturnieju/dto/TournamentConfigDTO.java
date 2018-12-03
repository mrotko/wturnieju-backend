package pl.wturnieju.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import pl.wturnieju.model.AccessOption;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.TournamentParticipantType;
import pl.wturnieju.model.TournamentSystemType;

public class TournamentConfigDTO {

    private List<AccessOption> accessOptions = Arrays.asList(AccessOption.values());

    private List<CompetitionType> competitionTypes = Arrays.asList(CompetitionType.values());

    private Map<CompetitionType, List<TournamentSystemType>> systemTypes = new EnumMap<>(CompetitionType.class);

    private Map<CompetitionType, List<TournamentParticipantType>> participantTypes = new EnumMap<>(
            CompetitionType.class);

    public TournamentConfigDTO() {
        initAvailableSystems();
        initParticipantTypes();
    }

    private void initParticipantTypes() {
        participantTypes.put(CompetitionType.CHESS, Collections.singletonList(TournamentParticipantType.SINGLE));
    }

    private void initAvailableSystems() {
        systemTypes.put(CompetitionType.CHESS, Arrays.asList(
                TournamentSystemType.SWISS,
                TournamentSystemType.ROUND_ROBIN
        ));
    }

    public Map<CompetitionType, List<TournamentSystemType>> getSystemTypes() {
        return systemTypes;
    }

    public List<AccessOption> getAccessOptions() {
        return accessOptions;
    }

    public void setAccessOptions(List<AccessOption> accessOptions) {
        this.accessOptions = accessOptions;
    }

    public void setSystemTypes(Map<CompetitionType, List<TournamentSystemType>> systemTypes) {
        this.systemTypes = systemTypes;
    }

    public List<CompetitionType> getCompetitionTypes() {
        return competitionTypes;
    }

    public void setCompetitionTypes(List<CompetitionType> competitionTypes) {
        this.competitionTypes = competitionTypes;
    }

    public Map<CompetitionType, List<TournamentParticipantType>> getParticipantTypes() {
        return participantTypes;
    }

    public void setParticipantTypes(Map<CompetitionType, List<TournamentParticipantType>> participantTypes) {
        this.participantTypes = participantTypes;
    }
}
