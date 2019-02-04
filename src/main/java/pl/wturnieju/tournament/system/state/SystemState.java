package pl.wturnieju.tournament.system.state;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.tournament.StageType;


@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemState extends Persistent {

    private Timestamp lastUpdate;

    private String tournamentId;

    private Map<StageType, List<Group>> stageToGroupsMapping = new EnumMap<>(StageType.class);

    private List<GameFixture> generatedGameFixtures = new ArrayList<>();

    @Transient
    public List<GameFixture> getAllGamesByStage(StageType stageType) {
        return stageToGroupsMapping.getOrDefault(stageType, Collections.emptyList()).stream()
                .map(Group::getAllGames)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Transient
    public List<GameFixture> getAllGames() {
        return stageToGroupsMapping.values().stream()
                .flatMap(Collection::stream)
                .map(Group::getAllGames)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Transient
    public List<Group> getAllGroups() {
        return stageToGroupsMapping.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
