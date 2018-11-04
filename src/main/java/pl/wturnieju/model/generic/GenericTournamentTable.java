package pl.wturnieju.model.generic;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Data;
import pl.wturnieju.model.IProfile;

@Data
public abstract class GenericTournamentTable<T extends TournamentTableRow> {

    private LocalDateTime lastUpdate;

    private Map<IProfile, T> profileToRowMap = new HashMap<>();

    public List<T> getFinalStandings() {
        return profileToRowMap.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
