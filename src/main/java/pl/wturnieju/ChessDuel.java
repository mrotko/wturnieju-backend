package pl.wturnieju;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessDuel extends Duel {

    private List<ChessMove> moves = new ArrayList<>();

    private String currentPlayerIdMove;

    private Map<String, Integer> playerIdToTimeLeftMap = new HashMap<>();

    private int timeBonus;
}
