package edu.hanover.sdp2026.core.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BoardTest {
    Board board = new Board();

    @Test
    void putPieceOnOccupiedSquareThrows() {
        Location location = Location.fromCoordinates(1, 1);
        board.putPiece(location, PieceType.Rook, Color.WHITE);

        assertThrows(RuntimeException.class, () -> board.putPiece(location, PieceType.Pawn, Color.BLACK));
    }

    @Test
    void makeMoveEmptiesFromAndFillsTo() {
        Location from = Location.fromCoordinates(4, 4);
        Location to = Location.fromCoordinates(4, 6);
        board.putPiece(from, PieceType.Rook, Color.WHITE);
        Piece rook = board.getPiece(from);

        board.makeMove(new Move(rook, from, to));

        assertNull(board.getPiece(from));
        assertSame(rook, board.getPiece(to));
    }

    @Test
    void makeMoveMarksPieceAsMoved() {
        Location from = Location.fromCoordinates(4, 4);
        Location to = Location.fromCoordinates(4, 6);
        board.putPiece(from, PieceType.Rook, Color.WHITE);
        Piece rook = board.getPiece(from);

        board.makeMove(new Move(rook, from, to));

        assertTrue(board.hasMoved(rook));
    }

    @Test
    void boardFromLeavesOriginalUntouched() {
        Location from = Location.fromCoordinates(4, 4);
        Location to = Location.fromCoordinates(4, 6);
        board.putPiece(from, PieceType.Rook, Color.WHITE);
        Piece rook = board.getPiece(from);

        Board derived = board.boardFrom(new Move(rook, from, to));

        assertSame(rook, board.getPiece(from));
        assertNull(board.getPiece(to));
        assertNull(derived.getPiece(from));
        assertSame(rook, derived.getPiece(to));
    }

    @Test
    void boardFromCarriesMovedState() {
        Location from = Location.fromCoordinates(4, 4);
        Location to = Location.fromCoordinates(4, 6);
        board.putPiece(from, PieceType.Rook, Color.WHITE);
        Piece rook = board.getPiece(from);
        board.setHasMoved(rook, true);

        Board derived = board.boardFrom(new Move(rook, from, to));

        assertTrue(derived.hasMoved(rook));
    }

    @Test
    void hasMovedIsFalseForNullPiece() {
        assertFalse(board.hasMoved(null));

        board.setHasMoved(null, true);

        assertFalse(board.hasMoved(null));
    }
}
