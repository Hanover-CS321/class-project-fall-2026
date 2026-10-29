package edu.hanover.sdp2026.core.game;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;

    @Nested
    class BoardTests {

        @Test
        void defaultConstructorLeavesBlankBoardAndWhiteTurn() {
            game = new Game();

            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    assertNull(game.getPiece(Location.fromCoordinates(col, row)));
                }
            }

            assertEquals(Color.WHITE, game.getTurn());
        }

        @Test
        void getPieceAndPutPieceWorkAsExpected() {
            game = new Game();

            Location location = Location.fromCoordinates(1, 1);
            game.putPiece(location, PieceType.King, Color.WHITE);

            assertLocationHasPiece(location, Color.WHITE, PieceType.King);
        }

        @Test
        void createDefaultGameSetsUpBoardCorrectlyAndWhiteTurn() {
            game = Game.createDefaultGame();

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
                assertLocationHasPiece(
                        Location.fromCoordinates(col, 1),
                        Color.WHITE,
                        backRow[col - 1]
                );

                assertLocationHasPiece(
                        Location.fromCoordinates(col, 2),
                        Color.WHITE,
                        PieceType.Pawn
                );

                assertLocationHasPiece(
                        Location.fromCoordinates(col, 7),
                        Color.BLACK,
                        PieceType.Pawn
                );

                assertLocationHasPiece(
                        Location.fromCoordinates(col, 8),
                        Color.BLACK,
                        backRow[col - 1]
                );
            }

            assertEquals(Color.WHITE, game.getTurn());
        }
    }

    @Nested
    class ComputeMovesTests {

    }

    @Nested
    class PawnMoveTests {

    }

    @Nested
    class RookMoveTests {

    }

    @Nested
    class KnightMoveTests {

    }

    @Nested
    class BishopMoveTests {

    }

    @Nested
    class QueenMoveTests {

    }

    @Nested
    class KingMoveTests {

    }

    private void assertLocationHasPiece(
            Location location,
            Color color,
            PieceType pieceType
    ) {
        assertEquals(color, game.getPiece(location).getColor());
        assertEquals(pieceType, game.getPiece(location).getType());
    }

    private void assertMoveExists(
            ArrayList<Move> moves,
            Location expectedLocation
    ) {
        boolean found = false;

        for (Move move : moves) {
            if (move.getTo().equals(expectedLocation)) {
                return;
            }
        }
        fail("move does not exist");
    }
}