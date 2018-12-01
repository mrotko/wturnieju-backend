package pl.wturnieju.model.chess;

import org.apache.commons.lang3.tuple.ImmutablePair;

import lombok.Data;
import pl.wturnieju.model.CompetitionType;
import pl.wturnieju.model.Fixture;

@Data
public class ChessFixture extends Fixture {

    //    private List<ChessMove> moves = new ArrayList<>();

    //    private String currentPlayerIdMove;

    //    private Map<String, Integer> playerIdToTimeLeftMap = new HashMap<>();

    //    private int timeBonus;

    public ChessFixture(ImmutablePair<String, String> playersIds) {
        super(playersIds);
        competitionType = CompetitionType.CHESS;
    }
}
