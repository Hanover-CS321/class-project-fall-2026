package edu.hanover.sdp2026.core.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {
    private final Map<Location, Piece> board;
    // Piece uses identity equality; these are the same instances held in board.
    private final Set<Piece> movedPieces;

    Board() {
        this.board = new HashMap<>();
        this.movedPieces = new HashSet<>();
    }

    private Board(Board oldBoard) {
        this.board = new HashMap<>(oldBoard.board);
        this.movedPieces = new HashSet<>(oldBoard.movedPieces);
    }

    Board boardFrom(Move move) {
        Board newBoard = new Board(this);
        newBoard.makeMove(move);
        return newBoard;
    }

    Set<LocatedPiece> getLocatedPieces() {
        Set<LocatedPiece> pieces = new HashSet<>();
        board.forEach((location, piece) -> pieces.add(new LocatedPiece(location, piece)));
        return pieces;
    }

    Piece getPiece(Location location) {
        return board.get(location);
    }

    void putPiece(Location location, PieceType type, Color color) {
        if (getPiece(location) != null) throw new RuntimeException("space is occupied");
        board.put(location, new Piece(type, color));
    }

    void makeMove(Move move) {
        board.remove(move.getFrom());
        board.put(move.getTo(), move.getPiece());
        movedPieces.add(move.getPiece());
    }

    boolean hasMoved(Piece piece) {
        return movedPieces.contains(piece);
    }

    void setHasMoved(Piece piece, boolean movedState) {
        if (piece == null) return;
        if (movedState) movedPieces.add(piece);
        else movedPieces.remove(piece);
    }
}
