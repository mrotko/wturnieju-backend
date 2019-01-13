package pl.wturnieju.tournament.system.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.Timestamp;


@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SystemState<T extends GameFixture> extends Persistent {

    private Timestamp lastUpdate;

    private List<String> teamsWithBye = new ArrayList<>();

    private String tournamentId;

    private Map<String, List<String>> teamsPlayedEachOther = new HashMap<>();

    private List<T> gameFixtures = new ArrayList<>();

    private List<T> generatedGameFixtures = new ArrayList<>();
}
