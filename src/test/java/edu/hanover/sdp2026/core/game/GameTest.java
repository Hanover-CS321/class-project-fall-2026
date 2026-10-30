package edu.hanover.sdp2026.core.game;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        @Test
        void computesAll8Possible () {
            Game game = new Game();
            Location loc = Location.fromString("d4");
            Piece knight = new Piece(PieceType.Knight, Color.WHITE);
            game.putPiece(loc, knight.getType(), knight.getColor());
            Location[] expectedMoves = {
                    loc.add(new Location.Shift(2, 1)),
                    loc.add(new Location.Shift(2, -1)),
                    loc.add(new Location.Shift(-2, 1)),
                    loc.add(new Location.Shift(-2, -1)),
                    loc.add(new Location.Shift(1, 2)),
                    loc.add(new Location.Shift(1, -2)),
                    loc.add(new Location.Shift(-1, 2)),
                    loc.add(new Location.Shift(-1, -2))
            };
            ArrayList<Move> moves = game.computeMoves(loc);

            Set<Location> actualMoves = createActualMoves(moves);
            Set<Location> expectedMoveSet = createExpectedMoveSet(expectedMoves);

            assertEquals(expectedMoveSet, actualMoves);
        }

        @Test
        void computeWithSomeOutOfBounds () {
            Game game = new Game();
            Location loc = Location.fromString("h4");
            Piece knight = new Piece(PieceType.Knight, Color.WHITE);
            game.putPiece(loc, knight.getType(), knight.getColor());

            Location[] expectedMoves = {
                    loc.add(new Location.Shift(-1, -2)), // g2
                    loc.add(new Location.Shift(-2, -1)), // f3
                    loc.add(new Location.Shift(-2, 1)),  // f5
                    loc.add(new Location.Shift(-1, 2))   // g6
            };
            ArrayList<Move> moves = game.computeMoves(loc);

            Set<Location> actualMoves = createActualMoves(moves);
            Set<Location> expectedMoveSet = createExpectedMoveSet(expectedMoves);
            assertEquals(expectedMoveSet, actualMoves);
        }

        private static Set<Location> createExpectedMoveSet(Location[] expectedMoves) {
            return new HashSet<>(Arrays.asList(expectedMoves));
        }

        private static Set<Location> createActualMoves(ArrayList<Move> moves) {
            Set<Location> actualMoves = new HashSet<>();
            for (Move move : moves) {
                actualMoves.add(move.getTo());
            }
            return actualMoves;
        }
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