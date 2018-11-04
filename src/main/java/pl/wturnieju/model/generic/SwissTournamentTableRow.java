package pl.wturnieju.model.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import lombok.Data;
import pl.wturnieju.model.IProfile;

@Data
public class SwissTournamentTableRow extends TournamentTableRow<SwissTournamentTableRow> {

    private double opponentsScore;

    public SwissTournamentTableRow(IProfile profile) {
        super(profile);
    }

    @Override
    protected List<Supplier<Integer>> getComparators(SwissTournamentTableRow o) {
        List<Supplier<Integer>> comparators = new ArrayList<>();

        comparators.add(() -> Double.compare(score, o.score));
        comparators.add(() -> Double.compare(opponentsScore, o.opponentsScore));
        comparators.add(() -> Integer.compare(wins, o.wins));
        comparators.add(() -> Integer.compare(draws, o.draws));

        return comparators;
    }
}
