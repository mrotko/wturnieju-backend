package pl.wturnieju.model.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import lombok.Data;

@Data
public class SwissTournamentTableRow extends TournamentTableRow<SwissTournamentTableRow> {

    private Double smallPoints;

    public SwissTournamentTableRow(String profileId) {
        super(profileId);
    }

    @Override
    protected List<Supplier<Integer>> getComparators(SwissTournamentTableRow o) {
        List<Supplier<Integer>> comparators = new ArrayList<>();

        comparators.add(() -> Double.compare(points, o.points));
        comparators.add(() -> Double.compare(smallPoints, o.smallPoints));
        comparators.add(() -> Integer.compare(wins, o.wins));
        comparators.add(() -> Integer.compare(draws, o.draws));

        return comparators;
    }
}
