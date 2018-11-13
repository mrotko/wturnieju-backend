package pl.wturnieju.model;

import pl.wturnieju.model.chess.ChessFixture;

public class FixtureBuilderFactory {
    public static FixtureBuilder getInstance(CompetitionType competitionType) {
        switch (competitionType) {
        case CHESS:
            return new FixtureBuilder(ChessFixture.class);
        default:
            throw new IllegalArgumentException("unknown competition type: + " + competitionType);
        }
    }
}
