package pl.wturnieju.model.generic;

import java.util.List;
import java.util.function.Supplier;

import lombok.Data;

@Data
public class RoundRobinTournamentTableRow extends TournamentTableRow<RoundRobinTournamentTableRow> {

    public RoundRobinTournamentTableRow(String profileId) {
        super(profileId);
    }

    @Override
    protected List<Supplier<Integer>> getComparators(RoundRobinTournamentTableRow o) {
        return null;
    }
}
