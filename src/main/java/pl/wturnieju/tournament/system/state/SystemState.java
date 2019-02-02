package pl.wturnieju.tournament.system.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.gamefixture.GameStatus;
import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.Timestamp;


@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SystemState<T extends GameFixture> extends Persistent {

    private Timestamp lastUpdate;

    private List<String> participantsWithBye = new ArrayList<>();

    private String tournamentId;

    private Map<String, List<String>> participantsPlayedEachOther = new HashMap<>();

    private List<T> gameFixtures = new ArrayList<>();

    private List<T> generatedGameFixtures = new ArrayList<>();

    public List<T> getEndedGames() {
        return gameFixtures.stream()
                .filter(game -> game.getGameStatus() == GameStatus.ENDED)
                .collect(Collectors.toList());
    }
}
