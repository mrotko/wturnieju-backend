package pl.wturnieju.model.generic;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Data;
import pl.wturnieju.model.Timestamp;

@Data
public abstract class GenericTournamentTable<T extends TournamentTableRow> {

    private Timestamp lastUpdate;

    protected List<T> rows = new ArrayList<>();

    @Transient
    public Optional<T> getProfileRow(String profileId) {
        return rows.stream()
                .filter(row -> row.profileId.equals(profileId))
                .findFirst();
    }

    public List<T> getFinalStandings() {
        return rows.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
