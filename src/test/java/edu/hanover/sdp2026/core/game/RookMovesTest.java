package edu.hanover.sdp2026.core.game;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class RookMovesTest {

    @Test
    void rookInMiddleOfEmptyBoardMovesHorizontallyAndVertically() {
        Game game = new Game();
        Location from = Location.fromString("d4");
        game.putPiece(from, PieceType.Rook, Color.WHITE);

        List<Move> moves = game.computeMoves(from);

        assertDestinations(
                moves,
                "a4", "b4", "c4", "e4", "f4", "g4", "h4",
                "d1", "d2", "d3", "d5", "d6", "d7", "d8"
        );

        for (Move move : moves) {
            assertEquals(from, move.getFrom());
            assertSame(game.getPiece(from), move.getPiece());
        }
    }

    @Test
    void rookInCornerDoesNotMoveOutsideBoard() {
        Game game = new Game();
        game.putPiece(
                Location.fromString("a1"),
                PieceType.Rook,
                Color.WHITE
        );

        List<Move> moves = game.computeMoves(Location.fromString("a1"));

        assertDestinations(
                moves,
                "b1", "c1", "d1", "e1", "f1", "g1", "h1",
                "a2", "a3", "a4", "a5", "a6", "a7", "a8"
        );
    }

    @Test
    void friendlyPieceBlocksRookAndCannotBeCaptured() {
        Game game = new Game();
        game.putPiece(
                Location.fromString("d4"),
                PieceType.Rook,
                Color.WHITE
        );
        game.putPiece(
                Location.fromString("d6"),
                PieceType.Pawn,
                Color.WHITE
        );

        List<Move> moves = game.computeMoves(Location.fromString("d4"));

        assertDestinations(
                moves,
                "a4", "b4", "c4", "e4", "f4", "g4", "h4",
                "d1", "d2", "d3", "d5"
        );
    }

    @Test
    void enemyPieceCanBeCaputredButBlocksFurtherMoves() {
        Game game = new Game();
        game.putPiece(
                Location.fromString("d4"),
                PieceType.Rook,
                Color.WHITE
        );
        game.putPiece(
                Location.fromString("f4"),
                PieceType.Pawn,
                Color.BLACK
        );

        List<Move> moves = game.computeMoves(Location.fromString("d4"));

        assertDestinations(
                moves,
                "a4", "b4", "c4", "e4", "f4",
                "d1", "d2", "d3", "d5", "d6", "d7", "d8"
        );
    }

    @Test
    void rookSurroundedByFriendlyPiecesHasNoMoves() {
        Game game = new Game();
        game.putPiece(
                Location.fromString("d4"),
                PieceType.Rook,
                Color.WHITE
        );

        for (String square : List.of("c4", "e4", "d3", "d5")) {
            game.putPiece(
                    Location.fromString(square),
                    PieceType.Pawn,
                    Color.WHITE
            );
        }

        List<Move> moves = game.computeMoves(Location.fromString("d4"));

        assertNotNull(moves);
        assertTrue(moves.isEmpty());
    }

    @Test
    void blackRookCanCaptureWhitePieceOnBlackTurn() {
        Game game = new Game();
        game.setTurn(Color.BLACK);
        game.putPiece(
                Location.fromString("d4"),
                PieceType.Rook,
                Color.BLACK
        );
        game.putPiece(
                Location.fromString("b4"),
                PieceType.Pawn,
                Color.WHITE
        );

        List<Move> moves = game.computeMoves(Location.fromString("d4"));

        assertDestinations(
                moves,
                "b4", "c4", "e4", "f4", "g4", "h4",
                "d1", "d2", "d3", "d5", "d6", "d7", "d8"
        );
    }

    private static void assertDestinations(
            List<Move> moves,
            String... expectedSquares
    ) {
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
        assertEquals(expected.size(), moves.size(), "Duplicte moves found");
    }
}
