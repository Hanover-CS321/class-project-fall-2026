package edu.hanover.sdp2026.core.game;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    HashMap<Location, Piece> board;
    Color turn;
    public Game() {
        setupInitialBoard();
        turn = Color.WHITE;
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


        for (int col = 0; col < 8; col++) {
            board.put(Location.fromCoordinates(col,0),
                    new Piece(backRow[col],Color.WHITE));

            board.put(Location.fromCoordinates(col,1),
                    new Piece(PieceType.Pawn,Color.WHITE));

            board.put(Location.fromCoordinates(col,6),
                    new Piece(PieceType.Pawn,Color.BLACK));

            board.put(Location.fromCoordinates(col,7),
                    new Piece(backRow[col],Color.BLACK));
        }
    }
}
