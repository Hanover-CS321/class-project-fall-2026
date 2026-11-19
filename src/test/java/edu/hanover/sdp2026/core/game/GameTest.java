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
        void putPieceThrowsWhenSquareIsOccupied() {
            game = new Game();

            Location location = Location.fromCoordinates(1, 1);
            game.putPiece(location, PieceType.King, Color.WHITE);

            assertThrows(RuntimeException.class, () -> game.putPiece(location, PieceType.Rook, Color.BLACK));
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
    class ComputeMovesTests {

        @Test
        void emptySquareReturnsNull() {
            Game game = new Game();

            assertNull(game.computeMoves(Location.fromString("d4")));
        }

        @Test
        void pieceCannotBeSelectedOnOpponentsTurn() {
            Game game = new Game();
            Location loc = Location.fromString("d4");
            game.putPiece(loc, PieceType.Rook, Color.WHITE);
            game.setTurn(Color.BLACK);

            assertNull(game.computeMoves(loc));
        }
    }

    @Nested
    class HasMovedTests {

        @Test
        void defaultBoardHasNotMoved() {
            game = Game.createDefaultGame();
            boolean move = game.hasMoved(game.getPiece(Location.fromCoordinates(2, 2)));
            boolean move2 = game.hasMoved(game.getPiece(Location.fromCoordinates(2, 1)));
            assertFalse(move);
            assertFalse(move2);
        }
    }

    @Nested
    class PawnMoveTests {

        @Test
        void whitePawnCanMoveOneOrTwoSpacesIfNotMoved() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(4, 2);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            Piece pawn = game.getPiece(pawnLocation);
            game.setHasMoved(pawn, false);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(2, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(4, 3));
            assertMoveExists(moves, Location.fromCoordinates(4, 4));
        }

        @Test
        void blackPawnCanMoveOneOrTwoSpacesIfNotMoved() {
            game = new Game();
            game.setTurn(Color.BLACK);

            Location pawnLocation = Location.fromCoordinates(4, 7);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.BLACK);

            Piece pawn = game.getPiece(pawnLocation);
            game.setHasMoved(pawn, false);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(2, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(4, 6));
            assertMoveExists(moves, Location.fromCoordinates(4, 5));
        }

        @Test
        void whitePawnCanOnlyMoveOneSpaceIfAlreadyMoved() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(4, 3);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            Piece pawn = game.getPiece(pawnLocation);
            game.setHasMoved(pawn, true);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(1, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(4, 4));
        }

        @Test
        void blackPawnCanOnlyMoveOneSpaceIfAlreadyMoved() {
            game = new Game();
            game.setTurn(Color.BLACK);

            Location pawnLocation = Location.fromCoordinates(4, 6);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.BLACK);

            Piece pawn = game.getPiece(pawnLocation);
            game.setHasMoved(pawn, true);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(1, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(4, 5));
        }

        @Test
        void pawnCannotMoveForwardWhenBlocked() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(4, 2);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            game.putPiece(Location.fromCoordinates(4, 3), PieceType.Rook, Color.BLACK);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(0, moves.size());
        }

        @Test
        void pawnCannotMoveTwoSpacesIfSecondSpaceIsBlocked() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(4, 2);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            game.putPiece(Location.fromCoordinates(4, 4), PieceType.Rook, Color.BLACK);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(1, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(4, 3));
        }

        @Test
        void whitePawnCanCaptureOpponentsDiagonally() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(4, 2);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            game.putPiece(Location.fromCoordinates(3, 3), PieceType.Rook, Color.BLACK);

            game.putPiece(Location.fromCoordinates(5, 3), PieceType.Rook, Color.BLACK);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(4, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(3, 3));
            assertMoveExists(moves, Location.fromCoordinates(4, 3));
            assertMoveExists(moves, Location.fromCoordinates(4, 4));
            assertMoveExists(moves, Location.fromCoordinates(5, 3));
        }

        @Test
        void blackPawnCanCaptureOpponentsDiagonally() {
            game = new Game();
            game.setTurn(Color.BLACK);

            Location pawnLocation = Location.fromCoordinates(4, 7);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.BLACK);

            game.putPiece(Location.fromCoordinates(3, 6), PieceType.Rook, Color.WHITE);

            game.putPiece(Location.fromCoordinates(5, 6), PieceType.Rook, Color.WHITE);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(4, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(3, 6));
            assertMoveExists(moves, Location.fromCoordinates(4, 6));
            assertMoveExists(moves, Location.fromCoordinates(4, 5));
            assertMoveExists(moves, Location.fromCoordinates(5, 6));
        }

        @Test
        void pawnCannotCaptureAlliesDiagonally() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(4, 2);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            game.putPiece(Location.fromCoordinates(3, 3), PieceType.Rook, Color.WHITE);

            game.putPiece(Location.fromCoordinates(5, 3), PieceType.Rook, Color.WHITE);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(2, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(4, 3));
            assertMoveExists(moves, Location.fromCoordinates(4, 4));
        }

        @Test
        void pawnOnLeftEdgeOnlyChecksValidDiagonal() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(1, 2);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            game.putPiece(Location.fromCoordinates(2, 3), PieceType.Rook, Color.BLACK);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(3, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(1, 3));
            assertMoveExists(moves, Location.fromCoordinates(1, 4));
            assertMoveExists(moves, Location.fromCoordinates(2, 3));
        }

        @Test
        void pawnOnRightEdgeOnlyChecksValidDiagonal() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(8, 2);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            game.putPiece(Location.fromCoordinates(7, 3), PieceType.Rook, Color.BLACK);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(3, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(8, 3));
            assertMoveExists(moves, Location.fromCoordinates(8, 4));
            assertMoveExists(moves, Location.fromCoordinates(7, 3));
        }

        @Test
        void whitePawnOnFinalRankHasNoMoves() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(4, 8);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertNotNull(moves);
            assertTrue(moves.isEmpty());
        }
    }

    @Nested
    class RookMoveTests {

        @Test
        void rookInMiddleOfEmptyBoardMovesHorizontallyAndVertically() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Rook, Color.WHITE);

            assertMoves(game, "d4", "a4", "b4", "c4", "e4", "f4", "g4", "h4", "d1", "d2", "d3", "d5", "d6", "d7", "d8");
        }

        @Test
        void rookInCornerDoesNotMoveOutsideBoard() {
            Game game = new Game();
            game.putPiece(Location.fromString("a1"), PieceType.Rook, Color.WHITE);

            assertMoves(game, "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "a2", "a3", "a4", "a5", "a6", "a7", "a8");
        }

        @Test
        void rookInOppositeCornerDoesNotMoveOutsideBoard() {
            Game game = new Game();
            game.putPiece(Location.fromString("h8"), PieceType.Rook, Color.WHITE);

            assertMoves(game, "h8", "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h1", "h2", "h3", "h4", "h5", "h6", "h7");
        }

        @Test
        void friendlyPieceBlocksRookAndCannotBeCaptured() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromString("d6"), PieceType.Pawn, Color.WHITE);

            assertMoves(game, "d4", "a4", "b4", "c4", "e4", "f4", "g4", "h4", "d1", "d2", "d3", "d5");
        }

        @Test
        void enemyPieceCanBeCapturedButBlocksFurtherMoves() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromString("f4"), PieceType.Pawn, Color.BLACK);

            assertMoves(game, "d4", "a4", "b4", "c4", "e4", "f4", "d1", "d2", "d3", "d5", "d6", "d7", "d8");
        }

        @Test
        void rookCannotJumpOverFriendlyPieceToCaptureEnemy() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromString("d5"), PieceType.Pawn, Color.WHITE);
            game.putPiece(Location.fromString("d7"), PieceType.Pawn, Color.BLACK);

            assertMoves(game, "d4", "a4", "b4", "c4", "e4", "f4", "g4", "h4", "d1", "d2", "d3");
        }

        @Test
        void rookIsBlockedOnBothSidesOfItsRank() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromString("b4"), PieceType.Pawn, Color.BLACK);
            game.putPiece(Location.fromString("f4"), PieceType.Pawn, Color.BLACK);

            assertMoves(game, "d4", "b4", "c4", "e4", "f4", "d1", "d2", "d3", "d5", "d6", "d7", "d8");
        }

        @Test
        void rookCanCaptureOnBoardEdge() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromString("h4"), PieceType.Pawn, Color.BLACK);

            assertMoves(game, "d4", "a4", "b4", "c4", "e4", "f4", "g4", "h4", "d1", "d2", "d3", "d5", "d6", "d7", "d8");
        }

        @Test
        void rookSurroundedByFriendlyPiecesHasNoMoves() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Rook, Color.WHITE);

            for (String square : new String[] {"c4", "e4", "d3", "d5"}) {
                game.putPiece(Location.fromString(square), PieceType.Pawn, Color.WHITE);
            }

            ArrayList<Move> moves = game.computeMoves(Location.fromString("d4"));

            assertNotNull(moves);
            assertTrue(moves.isEmpty());
        }

        @Test
        void blackRookCanCaptureWhitePieceOnBlackTurn() {
            Game game = new Game();
            game.setTurn(Color.BLACK);
            game.putPiece(Location.fromString("d4"), PieceType.Rook, Color.BLACK);
            game.putPiece(Location.fromString("b4"), PieceType.Pawn, Color.WHITE);

            assertMoves(game, "d4", "b4", "c4", "e4", "f4", "g4", "h4", "d1", "d2", "d3", "d5", "d6", "d7", "d8");
        }

        @Test
        void blackRookIsBlockedByBlackPiece() {
            Game game = new Game();
            game.setTurn(Color.BLACK);
            game.putPiece(Location.fromString("d4"), PieceType.Rook, Color.BLACK);
            game.putPiece(Location.fromString("d6"), PieceType.Pawn, Color.BLACK);

            assertMoves(game, "d4", "a4", "b4", "c4", "e4", "f4", "g4", "h4", "d1", "d2", "d3", "d5");
        }

        @Test
        void rookOnDefaultBoardIsBlockedByItsOwnPieces() {
            Game game = Game.createDefaultGame();

            ArrayList<Move> moves = game.computeMoves(Location.fromString("a1"));

            assertNotNull(moves);
            assertTrue(moves.isEmpty());
        }
    }

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
    class BishopMoveTests {

        @Test
        void bishopInMiddleOfEmptyBoardMovesDiagonally() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Bishop, Color.WHITE);

            assertMoves(game, "d4", "a1", "b2", "c3", "e5", "f6", "g7", "h8", "a7", "b6", "c5", "e3", "f2", "g1");
        }

        @Test
        void bishopInCornerDoesNotMoveOutsideBoard() {
            Game game = new Game();
            game.putPiece(Location.fromString("a1"), PieceType.Bishop, Color.WHITE);

            assertMoves(game, "a1", "b2", "c3", "d4", "e5", "f6", "g7", "h8");
        }

        @Test
        void friendlyPieceBlocksBishopAndCannotBeCaptured() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Bishop, Color.WHITE);
            game.putPiece(Location.fromString("f6"), PieceType.Pawn, Color.WHITE);

            assertMoves(game, "d4", "e5", "c5", "b6", "a7", "e3", "f2", "g1", "c3", "b2", "a1");
        }

        @Test
        void enemyPieceCanBeCapturedButBlocksFurtherMoves() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Bishop, Color.WHITE);
            game.putPiece(Location.fromString("f6"), PieceType.Pawn, Color.BLACK);

            assertMoves(game, "d4", "e5", "f6", "c5", "b6", "a7", "e3", "f2", "g1", "c3", "b2", "a1");
        }

        @Test
        void bishopSurroundedByFriendlyPiecesHasNoMoves() {
            Game game = new Game();
            game.putPiece(Location.fromString("d4"), PieceType.Bishop, Color.WHITE);

            for (String square : new String[] {"c3", "c5", "e3", "e5"}) {
                game.putPiece(Location.fromString(square), PieceType.Pawn, Color.WHITE);
            }

            ArrayList<Move> moves = game.computeMoves(Location.fromString("d4"));

            assertNotNull(moves);
            assertTrue(moves.isEmpty());
        }

        @Test
        void bishopOnDefaultBoardIsBlockedByItsOwnPieces() {
            Game game = Game.createDefaultGame();

            ArrayList<Move> moves = game.computeMoves(Location.fromString("c1"));

            assertNotNull(moves);
            assertTrue(moves.isEmpty());
        }

        @Test
        void blackBishopCanCaptureWhitePieceOnBlackTurn() {
            Game game = new Game();
            game.setTurn(Color.BLACK);
            game.putPiece(Location.fromString("d4"), PieceType.Bishop, Color.BLACK);
            game.putPiece(Location.fromString("b6"), PieceType.Pawn, Color.WHITE);

            assertMoves(game, "d4", "c5", "b6", "e5", "f6", "g7", "h8", "e3", "f2", "g1", "c3", "b2", "a1");
        }
    }

    @Nested
    class QueenMoveTests {
        @Test
        void noMoves() {
            Game game = Game.createDefaultGame();

            Location loc = Location.fromCoordinates(4, 1); // d1
            ArrayList<Move> moves = game.computeMoves(loc);

            Set<Location> actualMoves = createActualMoves(moves);

            assertEquals(Collections.emptySet(), actualMoves);
        }

        @Test
        void allMovesFromCenter() {
            Game game = new Game();
            Location loc = Location.fromString("d4");
            Piece queen = new Piece(PieceType.Queen, Color.WHITE);
            game.putPiece(loc, queen.getType(), queen.getColor());

            Set<Location> expected = Set.of(
                    // Horizontal
                    Location.fromCoordinates(1, 4), // a4
                    Location.fromCoordinates(2, 4), // b4
                    Location.fromCoordinates(3, 4), // c4
                    Location.fromCoordinates(5, 4), // e4
                    Location.fromCoordinates(6, 4), // f4
                    Location.fromCoordinates(7, 4), // g4
                    Location.fromCoordinates(8, 4), // h4

                    // Vertical
                    Location.fromCoordinates(4, 1), // d1
                    Location.fromCoordinates(4, 2), // d2
                    Location.fromCoordinates(4, 3), // d3
                    Location.fromCoordinates(4, 5), // d5
                    Location.fromCoordinates(4, 6), // d6
                    Location.fromCoordinates(4, 7), // d7
                    Location.fromCoordinates(4, 8), // d8

                    // Diagonal up-right
                    Location.fromCoordinates(5, 5), // e5
                    Location.fromCoordinates(6, 6), // f6
                    Location.fromCoordinates(7, 7), // g7
                    Location.fromCoordinates(8, 8), // h8

                    // Diagonal up-left
                    Location.fromCoordinates(3, 5), // c5
                    Location.fromCoordinates(2, 6), // b6
                    Location.fromCoordinates(1, 7), // a7

                    // Diagonal down-right
                    Location.fromCoordinates(5, 3), // e3
                    Location.fromCoordinates(6, 2), // f2
                    Location.fromCoordinates(7, 1), // g1

                    // Diagonal down-left
                    Location.fromCoordinates(3, 3), // c3
                    Location.fromCoordinates(2, 2), // b2
                    Location.fromCoordinates(1, 1) // a1
                    );

            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);

            assertEquals(expected, actualMoves);
        }

        @Test
        void allMovesFromCorner() {
            Game game = new Game();
            Location loc = Location.fromString("h1");
            Piece queen = new Piece(PieceType.Queen, Color.WHITE);
            game.putPiece(loc, queen.getType(), queen.getColor());

            Set<Location> expected = Set.of(
                    // Left
                    Location.fromCoordinates(1, 1),
                    Location.fromCoordinates(2, 1),
                    Location.fromCoordinates(3, 1),
                    Location.fromCoordinates(4, 1),
                    Location.fromCoordinates(5, 1),
                    Location.fromCoordinates(6, 1),
                    Location.fromCoordinates(7, 1),

                    // Up
                    Location.fromCoordinates(8, 2),
                    Location.fromCoordinates(8, 3),
                    Location.fromCoordinates(8, 4),
                    Location.fromCoordinates(8, 5),
                    Location.fromCoordinates(8, 6),
                    Location.fromCoordinates(8, 7),
                    Location.fromCoordinates(8, 8),

                    // Diagonal up-left
                    Location.fromCoordinates(7, 2),
                    Location.fromCoordinates(6, 3),
                    Location.fromCoordinates(5, 4),
                    Location.fromCoordinates(4, 5),
                    Location.fromCoordinates(3, 6),
                    Location.fromCoordinates(2, 7),
                    Location.fromCoordinates(1, 8));

            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);

            assertEquals(expected, actualMoves);
        }

        @Test
        void queenCanCaptureEnemies() {
            Game game = new Game();
            Location loc = Location.fromString("d4");
            Piece queen = new Piece(PieceType.Queen, Color.WHITE);
            game.putPiece(loc, queen.getType(), queen.getColor());

            game.putPiece(Location.fromCoordinates(4, 6), PieceType.Knight, Color.BLACK); // d6
            game.putPiece(Location.fromCoordinates(6, 6), PieceType.Bishop, Color.BLACK); // f6

            Set<Location> actualMoves = createActualMoves(game.computeMoves(loc));

            assertTrue(actualMoves.contains(Location.fromCoordinates(4, 6)));
            assertTrue(actualMoves.contains(Location.fromCoordinates(6, 6)));
        }

        @Test
        void queenOnlyThreeCaptureMoves() {
            Game game = new Game();

            Location loc = Location.fromString("a1");
            game.putPiece(loc, PieceType.Queen, Color.WHITE);

            game.putPiece(Location.fromString("a2"), PieceType.Pawn, Color.BLACK);
            game.putPiece(Location.fromString("b1"), PieceType.Pawn, Color.BLACK);
            game.putPiece(Location.fromString("b2"), PieceType.Pawn, Color.BLACK);

            Set<Location> expected =
                    Set.of(Location.fromString("a2"), Location.fromString("b1"), Location.fromString("b2"));

            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);

            assertEquals(expected, actualMoves);
        }

        @Test
        void queenCannotCaptureTeam() {
            Game game = new Game();

            Location loc = Location.fromString("a1");
            game.putPiece(loc, PieceType.Queen, Color.WHITE);

            game.putPiece(Location.fromString("a2"), PieceType.Pawn, Color.BLACK);
            game.putPiece(Location.fromString("b1"), PieceType.Pawn, Color.WHITE);
            game.putPiece(Location.fromString("b2"), PieceType.Pawn, Color.WHITE);

            Set<Location> expected = Set.of(Location.fromString("a2"));

            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);

            assertEquals(expected, actualMoves);
        }
    }

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

        @Test
        void kingThatHasMovedCannotCastle() {
            Game game = new Game();
            Location loc = Location.fromString("e1");
            game.putPiece(loc, PieceType.King, Color.WHITE);
            game.putPiece(Location.fromCoordinates(1, 1), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromCoordinates(8, 1), PieceType.Rook, Color.WHITE);
            game.setHasMoved(game.getPiece(loc), true);

            Set<Location> expected = Set.of(
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
        void kingCannotCastleWithRookThatHasMoved() {
            Game game = new Game();
            Location loc = Location.fromString("e1");
            game.putPiece(loc, PieceType.King, Color.WHITE);
            game.putPiece(Location.fromCoordinates(1, 1), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromCoordinates(8, 1), PieceType.Rook, Color.WHITE);
            game.setHasMoved(game.getPiece(Location.fromCoordinates(1, 1)), true);

            Set<Location> expected = Set.of(
                    Location.fromCoordinates(4, 1),
                    Location.fromCoordinates(6, 1),
                    Location.fromCoordinates(7, 1), // kingside castle still available
                    Location.fromCoordinates(4, 2),
                    Location.fromCoordinates(5, 2),
                    Location.fromCoordinates(6, 2));

            ArrayList<Move> moves = game.computeMoves(loc);
            Set<Location> actualMoves = createActualMoves(moves);
            assertEquals(expected, actualMoves);
        }

        @Test
        void kingCannotCastleThroughOccupiedSquare() {
            Game game = new Game();
            Location loc = Location.fromString("e1");
            game.putPiece(loc, PieceType.King, Color.WHITE);
            game.putPiece(Location.fromCoordinates(1, 1), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromCoordinates(8, 1), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromCoordinates(7, 1), PieceType.Knight, Color.WHITE); // g1 blocks kingside

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
        void queensideCastleIsBlockedWhenBFileIsOccupied() {
            Game game = new Game();
            Location loc = Location.fromString("e1");
            game.putPiece(loc, PieceType.King, Color.WHITE);
            game.putPiece(Location.fromCoordinates(1, 1), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromCoordinates(8, 1), PieceType.Rook, Color.WHITE);
            game.putPiece(Location.fromCoordinates(2, 1), PieceType.Knight, Color.WHITE); // b1 blocks queenside

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
    }

    private void assertLocationHasPiece(Location location, Color color, PieceType pieceType) {
        assertEquals(color, game.getPiece(location).getColor());
        assertEquals(pieceType, game.getPiece(location).getType());
    }

    private void assertMoveExists(ArrayList<Move> moves, Location expectedLocation) {
        for (Move move : moves) {
            if (move.getTo().equals(expectedLocation)) {
                return;
            }
        }
        fail("move does not exist");
    }

    private static void assertMoves(Game game, String fromSquare, String... expectedSquares) {
        Location from = Location.fromString(fromSquare);
        ArrayList<Move> moves = game.computeMoves(from);

        assertNotNull(moves);

        Set<Location> expected = new HashSet<>();
        for (String square : expectedSquares) {
            expected.add(Location.fromString(square));
        }

        assertEquals(expected, createActualMoves(moves), "Move destinations do not match");
        assertEquals(expected.size(), moves.size(), "Duplicate moves found");

        for (Move move : moves) {
            assertEquals(from, move.getFrom());
            assertSame(game.getPiece(from), move.getPiece());
        }
    }

    private static Set<Location> createActualMoves(ArrayList<Move> moves) {
        Set<Location> actualMoves = new HashSet<>();
        for (Move move : moves) {
            actualMoves.add(move.getTo());
        }
        return actualMoves;
    }
}
