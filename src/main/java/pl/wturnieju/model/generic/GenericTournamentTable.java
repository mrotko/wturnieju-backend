package pl.wturnieju.model.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import pl.wturnieju.model.Timestamp;

@Data
public abstract class GenericTournamentTable<T extends TournamentTableRow> {

    private Timestamp lastUpdate;

    protected List<T> rows = new ArrayList<>();

    public List<T> getFinalStandings() {
        return rows.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
