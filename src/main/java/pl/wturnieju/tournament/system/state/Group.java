package pl.wturnieju.tournament.system.state;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Data;
import pl.wturnieju.gamefixture.GameFixture;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.tournament.LegType;
import pl.wturnieju.tournament.Participant;
import pl.wturnieju.tournament.StageType;

@Data
public class Group {

    private String id;

    private Timestamp createdAt = Timestamp.now();

    private String name;

    private List<Participant> participants;

    private Map<LegType, List<GameFixture>> legToGamesMapping = new HashMap<>();

    private StageType stageType;

    @Transient
    public List<GameFixture> getAllGames() {
        return legToGamesMapping.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Transient
    public void addGame(GameFixture gameFixture) {
        legToGamesMapping.putIfAbsent(gameFixture.getLegType(), new ArrayList<>());
        legToGamesMapping.get(gameFixture.getLegType()).add(gameFixture);
    }

    @Transient
    public void deleteGame(GameFixture gameFixture) {
        var games = legToGamesMapping.getOrDefault(gameFixture.getLegType(), Collections.emptyList());
        games.removeIf(game -> game.getId().equals(gameFixture.getId()));
    }
}
