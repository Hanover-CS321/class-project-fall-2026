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
        return  game;
    }

    public ArrayList<Move> computeMoves(Location location){
        return new ArrayList<Move>();
    }

    public void makeMove(Move move){}

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
            board.put(Location.fromCoordinates(col,1),
                    new Piece(backRow[col],Color.WHITE));

            board.put(Location.fromCoordinates(col,2),
                    new Piece(PieceType.Pawn,Color.WHITE));

            board.put(Location.fromCoordinates(col,7),
                    new Piece(PieceType.Pawn,Color.BLACK));

            board.put(Location.fromCoordinates(col,8),
                    new Piece(backRow[col],Color.BLACK));
        }
    }
}
