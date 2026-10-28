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

    public ArrayList<Move> computeMoves(Location location) {
        Piece piece = getPiece(location);

        if (piece == null) return null;

        if (!isColorTurnOf(piece)) return null;

        return switch (piece.getType()) {
            case Pawn -> computePawnMoves(location, piece);
            case Rook -> computeRookMoves(location, piece);
            case Knight -> computeKnightMoves(location, piece);
            case Bishop -> computeBishopMoves(location, piece);
            case Queen -> computeQueenMoves(location, piece);
            case King -> computeKingMoves(location, piece);
            default -> throw new RuntimeException("no valid PieceType");
        };

    }

    public void makeMove(Move move) {
    }

    public Color getTurn() {
        return turn;
    }

    public void setTurn(Color turn) {
        this.turn = turn;
    }

    Piece getPiece(Location location) {
        return board.getOrDefault(location, null);
    }

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

    private boolean isColorTurnOf(Piece piece) {
        return piece.getColor() == turn;
    }

    private ArrayList<Move> computePawnMoves(Location location, Piece piece) {
        throw new RuntimeException("not implemented yet");
    }

    private ArrayList<Move> computeRookMoves(Location location, Piece piece) {
        ArrayList<Move> moves = new ArrayList<>();

        int[][] directions = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        for (int[] direction : directions) {
            int col = location.col() + direction[0];
            int row = location.row() + direction[1];

            while (col >= 1 && col <= 8 && row >= 1 && row <= 8) {
                Location destination = Location.fromCoordinates(col, row);
                Piece occupant = getPiece(destination);

                if (occupant == null) {
                    moves.add(new Move(piece, location, destination));
                } else {
                    if (occupant.getColor() != piece.getColor()) {
                        moves.add(new Move(piece, location, destination));
                    }

                    break;
                }

                col += direction[0];
                row += direction[1];
            }
        }

        return moves;
    }

    private ArrayList<Move> computeKnightMoves(Location location, Piece piece) {
        throw new RuntimeException("not implemented yet");
    }

    private ArrayList<Move> computeBishopMoves(Location location, Piece piece) {
        throw new RuntimeException("not implemented yet");
    }

    private ArrayList<Move> computeQueenMoves(Location location, Piece piece) {
        throw new RuntimeException("not implemented yet");
    }

    private ArrayList<Move> computeKingMoves(Location location, Piece piece) {
        throw new RuntimeException("not implemented yet");
    }
}
