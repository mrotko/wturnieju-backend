package pl.wturnieju.model.chess;

public enum ChessPiece {

    KING(0),
    QUEEN(9),
    ROOK(5),
    BISHOP(3),
    KNIGHT(3),
    PAWN(1);

    private ChessPiece(int value) {
        this.value = value;
    }

    private int value;
}
