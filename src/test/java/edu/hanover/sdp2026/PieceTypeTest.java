package edu.hanover.sdp2026;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTypeTest {

    @Test
    void abbreviationIsRight() {
        assertEquals("", PieceType.Pawn.getAbbr());
        assertEquals("R", PieceType.Rook.getAbbr());
        assertEquals("N", PieceType.Knight.getAbbr());
        assertEquals("B", PieceType.Bishop.getAbbr());
        assertEquals("K", PieceType.King.getAbbr());
        assertEquals("Q", PieceType.Queen.getAbbr());
    }

}