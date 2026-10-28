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

        @Test
        void computeMovesReturnsCorrectFromAndPiece() {
            game = new Game();

            Location location = Location.fromCoordinates(4, 2);
            game.putPiece(location, PieceType.Pawn, Color.WHITE);

            Piece piece = game.getPiece(location);

            ArrayList<Move> moves = game.computeMoves(location);

            for (Move move : moves) {
                assertEquals(location, move.getFrom());
                assertSame(piece, move.getPiece());
            }
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
            pawn.setHasMoved(false);

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
            pawn.setHasMoved(false);

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
            pawn.setHasMoved(true);

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
            pawn.setHasMoved(true);

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(1, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(4, 5));
        }

        @Test
        void pawnCannotMoveForwardWhenBlocked() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(4, 2);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            game.putPiece(
                    Location.fromCoordinates(4, 3),
                    PieceType.Rook,
                    Color.BLACK
            );

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(0, moves.size());
        }

        @Test
        void pawnCannotMoveTwoSpacesIfSecondSpaceIsBlocked() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(4, 2);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            game.putPiece(
                    Location.fromCoordinates(4, 4),
                    PieceType.Rook,
                    Color.BLACK
            );

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(1, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(4, 3));
        }

        @Test
        void whitePawnCanCaptureOpponentsDiagonally() {
            game = new Game();

            Location pawnLocation = Location.fromCoordinates(4, 2);
            game.putPiece(pawnLocation, PieceType.Pawn, Color.WHITE);

            game.putPiece(
                    Location.fromCoordinates(3, 3),
                    PieceType.Rook,
                    Color.BLACK
            );

            game.putPiece(
                    Location.fromCoordinates(5, 3),
                    PieceType.Rook,
                    Color.BLACK
            );

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

            game.putPiece(
                    Location.fromCoordinates(3, 6),
                    PieceType.Rook,
                    Color.WHITE
            );

            game.putPiece(
                    Location.fromCoordinates(5, 6),
                    PieceType.Rook,
                    Color.WHITE
            );

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

            game.putPiece(
                    Location.fromCoordinates(3, 3),
                    PieceType.Rook,
                    Color.WHITE
            );

            game.putPiece(
                    Location.fromCoordinates(5, 3),
                    PieceType.Rook,
                    Color.WHITE
            );

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

            game.putPiece(
                    Location.fromCoordinates(2, 3),
                    PieceType.Rook,
                    Color.BLACK
            );

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

            game.putPiece(
                    Location.fromCoordinates(7, 3),
                    PieceType.Rook,
                    Color.BLACK
            );

            ArrayList<Move> moves = game.computeMoves(pawnLocation);

            assertEquals(3, moves.size());
            assertMoveExists(moves, Location.fromCoordinates(8, 3));
            assertMoveExists(moves, Location.fromCoordinates(8, 4));
            assertMoveExists(moves, Location.fromCoordinates(7, 3));
        }
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
                found = true;
                break;
            }
        }

        assertTrue(found);
    }
}