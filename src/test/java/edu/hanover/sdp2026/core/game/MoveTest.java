package edu.hanover.sdp2026.core.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    @Test
    public void makeMoveClass () {
        Piece p = new Piece(PieceType.Rook, Color.WHITE);
        Location loc1 = Location.fromCoordinates(2, 2);
        Location loc2 = Location.fromCoordinates( 2, 4);
        Move m = new Move(p, loc1, loc2);
        assertTrue(assertHasSame(m, new Move(p, loc1, loc2)));
    }

    private boolean assertHasSame(Move m, Move move) {
        if (m.getTo() == move. getTo() && m.getFrom() == move.getFrom()) {
            return true;
        }
        return false;
    }

}