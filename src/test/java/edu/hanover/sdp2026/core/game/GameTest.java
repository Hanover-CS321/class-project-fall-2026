package edu.hanover.sdp2026.core.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
                assertLocationHasPiece(Location.fromCoordinates(col, 1), Color.WHITE, backRow[col - 1]);

                assertLocationHasPiece(Location.fromCoordinates(col, 2), Color.WHITE, PieceType.Pawn);

                assertLocationHasPiece(Location.fromCoordinates(col, 7), Color.BLACK, PieceType.Pawn);

                assertLocationHasPiece(Location.fromCoordinates(col, 8), Color.BLACK, backRow[col - 1]);
            }

            assertEquals(Color.WHITE, game.getTurn());
        }
    }

    @Nested
    class ComputeMovesTests {}

    @Nested
    class PawnMoveTests {}

    @Nested
    class RookMoveTests {}

    @Nested
    class KnightMoveTests {
        @Test
        void computesAll8Possible() {
            Game game = new Game();
            Location loc = Location.fromString("d4");
            Piece knight = new Piece(PieceType.Knight, Color.WHITE);
            game.putPiece(loc, knight.getType(), knight.getColor());
            Set<Location> expected = Set.of(
                    Location.fromCoordinates(6, 5),
                    Location.fromCoordinates(6, 3),
                    Location.fromCoordinates(2, 5),
                    Location.fromCoordinates(2, 3),
                    Location.fromCoordinates(5, 6),
                    Location.fromCoordinates(5, 2),
                    Location.fromCoordinates(3, 6),
                    Location.fromCoordinates(3, 2));
            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void computeWithSomeOutOfBounds() {
            Game game = new Game();
            Location loc = Location.fromString("h4");
            Piece knight = new Piece(PieceType.Knight, Color.WHITE);
            game.putPiece(loc, knight.getType(), knight.getColor());

            Set<Location> expected = Set.of(
                    Location.fromCoordinates(7, 2), // g2
                    Location.fromCoordinates(6, 3), // f3
                    Location.fromCoordinates(6, 5), // f5
                    Location.fromCoordinates(7, 6) // g6
                    );

            ArrayList<Move> moves = game.computeMoves(loc);

            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void enemyInWay() {
            Game game = new Game();
            Location loc = Location.fromString("h4");
            Piece knight = new Piece(PieceType.Knight, Color.WHITE);
            game.putPiece(loc, knight.getType(), knight.getColor());
            game.putPiece(Location.fromCoordinates(7, 6), PieceType.Knight, Color.BLACK);
            game.putPiece(Location.fromCoordinates(7, 2), PieceType.Knight, Color.BLACK);

            Set<Location> expected = Set.of(
                    Location.fromCoordinates(7, 2), // g2
                    Location.fromCoordinates(6, 3), // f3
                    Location.fromCoordinates(6, 5), // f5
                    Location.fromCoordinates(7, 6) // g6
                    );
            ArrayList<Move> moves = game.computeMoves(loc);

            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void teamInWay() {
            Game game = new Game();
            Location loc = Location.fromString("h4");
            Piece knight = new Piece(PieceType.Knight, Color.WHITE);
            game.putPiece(loc, knight.getType(), knight.getColor());
            game.putPiece(Location.fromCoordinates(7, 6), PieceType.Knight, Color.WHITE);
            game.putPiece(Location.fromCoordinates(7, 2), PieceType.Knight, Color.WHITE);

            Set<Location> expected = Set.of(
                    Location.fromCoordinates(6, 3), // f3
                    Location.fromCoordinates(6, 5) // f5
                    );
            ArrayList<Move> moves = game.computeMoves(loc);

            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }
    }

    @Nested
    class BishopMoveTests {}

    @Nested
    class QueenMoveTests {}

    @Nested
    class KingMoveTests {
        @Test
        void noMoves() {
            Game game = Game.createDefaultGame();

            Location loc = Location.fromCoordinates(5, 1); // e1
            ArrayList<Move> moves = game.computeMoves(loc);

            Set<Location> actualMoves = createActualMoves(moves);

            assertEquals(Collections.emptySet(), actualMoves);
        }

        @Test
        void allSpotsAroundKing() {
            Game game = new Game();
            Location loc = Location.fromString("d4");
            Piece king = new Piece(PieceType.King, Color.WHITE);
            game.putPiece(loc, king.getType(), king.getColor());
            Set<Location> expected = Set.of(
                    Location.fromCoordinates(3, 3),
                    Location.fromCoordinates(3, 4),
                    Location.fromCoordinates(3, 5),
                    Location.fromCoordinates(4, 3),
                    Location.fromCoordinates(4, 5),
                    Location.fromCoordinates(5, 3),
                    Location.fromCoordinates(5, 4),
                    Location.fromCoordinates(5, 5));
            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void spotsAroundKingInCorner() {
            Game game = new Game();
            Location loc = Location.fromString("h1");
            Piece king = new Piece(PieceType.King, Color.WHITE);
            game.putPiece(loc, king.getType(), king.getColor());
            Set<Location> expected = Set.of(
                    Location.fromCoordinates(7, 1), Location.fromCoordinates(7, 2), Location.fromCoordinates(8, 2));
            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void spotsAroundKingWithEnemies() {
            Game game = new Game();
            Location loc = Location.fromString("h1");
            Piece king = new Piece(PieceType.King, Color.WHITE);
            game.putPiece(loc, king.getType(), king.getColor());
            game.putPiece(Location.fromCoordinates(7, 1), PieceType.Knight, Color.BLACK);
            game.putPiece(Location.fromCoordinates(7, 2), PieceType.Knight, Color.BLACK);
            Set<Location> expected = Set.of(
                    Location.fromCoordinates(7, 1), Location.fromCoordinates(7, 2), Location.fromCoordinates(8, 2));
            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void spotsAroundKingWithTeam() {
            Game game = new Game();
            Location loc = Location.fromString("h1");
            Piece king = new Piece(PieceType.King, Color.WHITE);
            game.putPiece(loc, king.getType(), king.getColor());
            game.putPiece(Location.fromCoordinates(7, 1), PieceType.Knight, Color.WHITE);
            game.putPiece(Location.fromCoordinates(7, 2), PieceType.Knight, Color.WHITE);
            Set<Location> expected = Set.of(Location.fromCoordinates(8, 2));
            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void allSpotsAndCastleBothWhite() {
            Game game = new Game();
            Location loc = Location.fromString("e1");
            Piece king = new Piece(PieceType.King, Color.WHITE);
            game.putPiece(loc, king.getType(), king.getColor());
            Piece rook = new Piece(PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromCoordinates(1, 1), rook.getType(), rook.getColor());
            game.putPiece(Location.fromCoordinates(8, 1), rook.getType(), rook.getColor());
            Set<Location> expected = Set.of(
                    Location.fromCoordinates(3, 1),
                    Location.fromCoordinates(4, 1),
                    Location.fromCoordinates(6, 1),
                    Location.fromCoordinates(7, 1),
                    Location.fromCoordinates(4, 2),
                    Location.fromCoordinates(5, 2),
                    Location.fromCoordinates(6, 2));
            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void whiteKingCanOnlyCastleLeft() {
            Game game = new Game();
            Location loc = Location.fromString("e1");
            game.putPiece(loc, PieceType.King, Color.WHITE);
            game.putPiece(Location.fromCoordinates(1, 1), PieceType.Rook, Color.WHITE);

            Set<Location> expected = Set.of(
                    Location.fromCoordinates(3, 1), // queenside castle
                    Location.fromCoordinates(4, 1),
                    Location.fromCoordinates(6, 1),
                    Location.fromCoordinates(4, 2),
                    Location.fromCoordinates(5, 2),
                    Location.fromCoordinates(6, 2));

            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void whiteKingCanOnlyCastleRight() {
            Game game = new Game();
            Location loc = Location.fromString("e1");
            game.putPiece(loc, PieceType.King, Color.WHITE);
            game.putPiece(Location.fromCoordinates(8, 1), PieceType.Rook, Color.WHITE);

            Set<Location> expected = Set.of(
                    Location.fromCoordinates(4, 1),
                    Location.fromCoordinates(6, 1),
                    Location.fromCoordinates(7, 1), // kingside castle
                    Location.fromCoordinates(4, 2),
                    Location.fromCoordinates(5, 2),
                    Location.fromCoordinates(6, 2));

            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void blackKingCanOnlyCastleLeft() {
            Game game = new Game();
            game.setTurn(Color.BLACK);
            Location loc = Location.fromString("e8");
            game.putPiece(loc, PieceType.King, Color.BLACK);
            game.putPiece(Location.fromCoordinates(1, 8), PieceType.Rook, Color.BLACK);

            Set<Location> expected = Set.of(
                    Location.fromCoordinates(3, 8), // queenside castle
                    Location.fromCoordinates(4, 8),
                    Location.fromCoordinates(6, 8),
                    Location.fromCoordinates(4, 7),
                    Location.fromCoordinates(5, 7),
                    Location.fromCoordinates(6, 7));

            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void blackKingCanOnlyCastleRight() {
            Game game = new Game();
            game.setTurn(Color.BLACK);
            Location loc = Location.fromString("e8");
            game.putPiece(loc, PieceType.King, Color.BLACK);
            game.putPiece(Location.fromCoordinates(8, 8), PieceType.Rook, Color.BLACK);

            Set<Location> expected = Set.of(
                    Location.fromCoordinates(4, 8),
                    Location.fromCoordinates(6, 8),
                    Location.fromCoordinates(7, 8), // kingside castle
                    Location.fromCoordinates(4, 7),
                    Location.fromCoordinates(5, 7),
                    Location.fromCoordinates(6, 7));

            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void blackKingCanCastleBothWays() {
            Game game = new Game();
            game.setTurn(Color.BLACK);
            Location loc = Location.fromString("e8");
            game.putPiece(loc, PieceType.King, Color.BLACK);
            game.putPiece(Location.fromCoordinates(1, 8), PieceType.Rook, Color.BLACK);
            game.putPiece(Location.fromCoordinates(8, 8), PieceType.Rook, Color.BLACK);

            Set<Location> expected = Set.of(
                    Location.fromCoordinates(3, 8), // queenside castle
                    Location.fromCoordinates(4, 8),
                    Location.fromCoordinates(6, 8),
                    Location.fromCoordinates(7, 8), // kingside castle
                    Location.fromCoordinates(4, 7),
                    Location.fromCoordinates(5, 7),
                    Location.fromCoordinates(6, 7));

            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }
    }

    private void assertLocationHasPiece(Location location, Color color, PieceType pieceType) {
        assertEquals(color, game.getPiece(location).getColor());
        assertEquals(pieceType, game.getPiece(location).getType());
    }

    private void assertMoveExists(ArrayList<Move> moves, Location expectedLocation) {
        boolean found = false;

        for (Move move : moves) {
            if (move.getTo().equals(expectedLocation)) {
                return;
            }
        }
        fail("move does not exist");
    }

    private static Set<Location> createActualMoves(ArrayList<Move> moves) {
        Set<Location> actualMoves = new HashSet<>();
        for (Move move : moves) {
            actualMoves.add(move.getTo());
        }
        return actualMoves;
    }
}
