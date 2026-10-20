package edu.hanover.sdp2026.core.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    @Test
    public void makeMoveClass () {
        Piece p = new Piece(PieceType.Rook, Color.WHITE);
        Location loc1 = Location.fromCoordinates(2, 2);
        Location loc2 = Location.fromCoordinates( 2, 4);
        Move move = new Move(p, loc1, loc2);
        assertEquals(loc1, move.getFrom());
        assertEquals(loc2, move.getTo());
        assertEquals(p, move.getPiece());
    }

    @Test
    void testMoveAsString() {
        Piece p = new Piece(PieceType.Rook, Color.WHITE);
        Location loc1 = Location.fromCoordinates(2, 2);
        Location loc2 = Location.fromCoordinates( 2, 4);
        Move move =  new Move(p, loc1, loc2);
        assertEquals("Rb4", move.moveAsString());
    }
    
}