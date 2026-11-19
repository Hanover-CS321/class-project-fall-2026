package edu.hanover.sdp2026.core.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class BishopMovesTest {

    @Test
    void bishopInMiddleOfEmptyBoardMovesDiagonally() {
        Game game = new Game();
        Location from = Location.fromString("d4");
        game.putPiece(from, PieceType.Bishop, Color.WHITE);

        List<Move> moves = game.computeMoves(from);

        assertDestinations(moves, "a1", "b2", "c3", "e5", "f6", "g7", "h8", "a7", "b6", "c5", "e3", "f2", "g1");

        for (Move move : moves) {
            assertEquals(from, move.getFrom());
            assertSame(game.getPiece(from), move.getPiece());
        }
    }

    private static void assertDestinations(List<Move> moves, String... expectedSquares) {

        assertNotNull(moves);

        Set<Location> expected = new HashSet<>();
        for (String square : expectedSquares) {
            expected.add(Location.fromString(square));
        }

        Set<Location> actual = new HashSet<>();
        for (Move move : moves) {
            actual.add(move.getTo());
        }

        assertEquals(expected, actual, "Move destinations do not match");

        assertEquals(expected.size(), moves.size(), "Duplicate moves found");
    }
}
