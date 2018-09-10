package pl.wturnieju.model.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wturnieju.model.Duel;

public class ChessDuel extends Duel {

    private List<ChessMove> moves = new ArrayList<>();

    private String currentPlayerIdMove;

    private Map<String, Integer> playerIdToTimeLeftMap = new HashMap<>();

    private int timeBonus;
}
