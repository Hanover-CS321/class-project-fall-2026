package edu.hanover.sdp2026.core.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void makeMove(Move move) {}

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
        throw new RuntimeException("not implemented yet");
    }

    private ArrayList<Move> computeKnightMoves(Location location, Piece piece) {
        // all 8 possibilities
        ArrayList<Move> validMoves = new ArrayList<>();
        List<Location.Shift> knightShifts = List.of(
                new Location.Shift(2, 1),
                new Location.Shift(2, -1),
                new Location.Shift(-2, 1),
                new Location.Shift(-2, -1),
                new Location.Shift(1, 2),
                new Location.Shift(1, -2),
                new Location.Shift(-1, 2),
                new Location.Shift(-1, -2));
        List<Location> valid = location.getValidDestinations(knightShifts);
        for (Location l : valid) {
            if (getPiece(l) == null || getPiece(l).getColor() != piece.getColor()) {
                validMoves.add(new Move(piece, location, l));
            }
        }
        return validMoves;
    }

    private ArrayList<Move> computeBishopMoves(Location location, Piece piece) {
        throw new RuntimeException("not implemented yet");
    }

    private ArrayList<Move> computeQueenMoves(Location location, Piece piece) {
        // Queen moves is just bishop combine with rook
        ArrayList<Move> queenMoves = computeBishopMoves(location, piece);
        queenMoves.addAll(computeRookMoves(location, piece));
        return queenMoves;
        // throw new RuntimeException("not implemented yet");
    }

    private ArrayList<Move> computeKingMoves(Location location, Piece piece) {
        ArrayList<Move> validMoves = new ArrayList<>();
        List<Location.Shift> kingShifts = List.of(
                new Location.Shift(1, 1),
                new Location.Shift(1, -1),
                new Location.Shift(1, 0),
                new Location.Shift(-1, 1),
                new Location.Shift(-1, -1),
                new Location.Shift(-1, 0),
                new Location.Shift(0, 1),
                new Location.Shift(0, -1));
        List<Location> valid = location.getValidDestinations(kingShifts);

        for (Location l : valid) {
            if (getPiece(l) == null || getPiece(l).getColor() != piece.getColor()) {
                validMoves.add(new Move(piece, location, l));
            }
        }
        // king always on file e if unmoved
        if (!piece.hasMoved()) { // will be at e1 for white
            if (piece.getColor() == Color.WHITE) { // get white King Castle
                int row = 1;
                addCastles(location, piece, validMoves, row);
            } else { // get black king castle, will be at e8
                int row = 8;
                addCastles(location, piece, validMoves, row);
            }
        }
        return validMoves;
    }

    private void addCastles(Location location, Piece piece, ArrayList<Move> validMoves, int row) {
        Location qRook = Location.fromCoordinates(1, row); // confirm queen side rook and hasn't moved
        if (isUnMovedRook(qRook)) {
            Location loc2 = Location.fromCoordinates(2, row); // no piece
            Location loc3 = Location.fromCoordinates(3, row); // no piece, cant figure out shifts
            Location loc4 = Location.fromCoordinates(4, row); // no piece
            if (getPiece(loc2) == null && getPiece(loc3) == null && getPiece(loc4) == null) {
                validMoves.add(new Move(piece, location, loc3));
            }
        } // getPiece can't take an optional

        Location kRook = Location.fromCoordinates(8, row); // confirm king side rook and hasn't moved
        if (isUnMovedRook(kRook)) {
            Location kloc2 = Location.fromCoordinates(6, row); // no piece
            Location kloc3 = Location.fromCoordinates(7, row); // no piece
            if (getPiece(kloc2) == null && getPiece(kloc3) == null) {
                validMoves.add(new Move(piece, location, kloc3));
            }
        }
    }

    private boolean isUnMovedRook(Location rookMaybe) {
        if (getPiece(rookMaybe) != null) {
            return getPiece(rookMaybe).getType() == PieceType.Rook
                    && !getPiece(rookMaybe).hasMoved();
        } else {
            return false;
        }
    }
}
