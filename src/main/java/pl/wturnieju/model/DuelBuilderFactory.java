package pl.wturnieju.model;

import pl.wturnieju.model.chess.ChessDuel;

public class DuelBuilderFactory {
    public static DuelBuilder getInstance(CompetitionType competitionType) {
        switch (competitionType) {
        case CHESS:
            return new DuelBuilder(ChessDuel.class);
        default:
            throw new IllegalArgumentException("unknown competition type: + " + competitionType);
        }
    }
}
