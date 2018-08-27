package pl.wturnieju;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class ChessMove {

    private final String playerId;

    private final int moveNumber;

    private final int gameMoveNumber;

    private final ChessPiece piece;

    private final String oldField;

    private final String newField;

    private final boolean check;

    private final boolean checkmate;

    private final boolean bad;
}
