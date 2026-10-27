package edu.hanover.sdp2026.core.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private Map<Location, Piece> board = new HashMap<>();
    private Color turn = Color.WHITE;

    public Game() {}

    public static Game createDefaultGame() {
        Game game = new Game();
        game.setupInitialBoard();
        return game;
    }

    public ArrayList<Move> computeMoves(Location location){
        return new ArrayList<Move>();
    }

    public void makeMove(Move move){}
    void putPiece(Location location, PieceType type, Color color) {
        if (getPiece(location) != null) throw new RuntimeException("space is occupied");
        board.put(location, new Piece(type, color));
    }

    private void setupInitialBoard() {
        PieceType[] backRow = {
                PieceType.Rook,
                PieceType.Knight,
                PieceType.Bishop,
                PieceType.Queen,
                PieceType.King,
                PieceType.Bishop,
                PieceType.Knight,
                PieceType.Rook
        };


        for (int col = 1; col <= 8; col++) {
            putPiece(Location.fromCoordinates(col, 1), backRow[col - 1], Color.WHITE);

            putPiece(Location.fromCoordinates(col, 2), PieceType.Pawn, Color.WHITE);

            putPiece(Location.fromCoordinates(col, 7), PieceType.Pawn, Color.BLACK);

            putPiece(Location.fromCoordinates(col, 8), backRow[col - 1], Color.BLACK);
        }
    }
    }
}
