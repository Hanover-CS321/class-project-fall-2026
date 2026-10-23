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
        Piece piece = getPiece(location);
        
        if(piece == null) return null;

        if(!isColorTurnOf(piece)) return null;

        return switch(piece.getType()) {
            case Pawn -> computePawnMoves(location, piece);
            case Rook -> computeRookMoves(location, piece);
            case Knight -> computeKnightMoves(location, piece);
            case Bishop -> computeBishopMoves(location, piece);
            case Queen -> computeQueenMoves(location, piece);
            case King -> computeKingMoves(location, piece);
            default -> throw new RuntimeException("no valid PieceType");
        };

    }

    private boolean isColorTurnOf(Piece piece) {
        return piece.getColor() == turn;
    }

    Piece getPiece(Location location) {
        return board.getOrDefault(location, null);
    }

    void putPiece(Location location, PieceType type, Color color){
        if(getPiece(location) != null) throw new RuntimeException("space is occupied");
        board.put(location, new Piece(type, color));
    }
    
    public ArrayList<Move> computePawnMoves(Location location, Piece piece){
        throw new RuntimeException("not implemented yet");
    }

    public ArrayList<Move> computeRookMoves(Location location, Piece piece){
        throw new RuntimeException("not implemented yet");
    }

    public ArrayList<Move> computeKnightMoves(Location location, Piece piece){
        throw new RuntimeException("not implemented yet");
    }

    public ArrayList<Move> computeBishopMoves(Location location, Piece piece){
        throw new RuntimeException("not implemented yet");
    }

    public ArrayList<Move> computeQueenMoves(Location location, Piece piece){
        throw new RuntimeException("not implemented yet");
    }

    public ArrayList<Move> computeKingMoves(Location location, Piece piece){
        throw new RuntimeException("not implemented yet");
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
                    new Piece(backRow[col-1],Color.WHITE));

            board.put(Location.fromCoordinates(col,2),
                    new Piece(PieceType.Pawn,Color.WHITE));

            board.put(Location.fromCoordinates(col,7),
                    new Piece(PieceType.Pawn,Color.BLACK));

            board.put(Location.fromCoordinates(col,8),
                    new Piece(backRow[col-1],Color.BLACK));
        }
    }
}
