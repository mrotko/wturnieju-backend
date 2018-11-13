package pl.wturnieju.model.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import pl.wturnieju.model.Fixture;
import pl.wturnieju.model.IProfile;

public class ChessFixture extends Fixture {

    private List<ChessMove> moves = new ArrayList<>();

    private String currentPlayerIdMove;

    private Map<String, Integer> playerIdToTimeLeftMap = new HashMap<>();

    private int timeBonus;

    public ChessFixture(ImmutablePair<IProfile, IProfile> players) {
        super(players);
    }
}
