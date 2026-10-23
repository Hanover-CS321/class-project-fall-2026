package edu.hanover.sdp2026.core.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
Game game;

    @Test
    void defaultConstructorLeavesBlankBoardAndWhiteTurn() {
        game = new Game();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                assertNull(game.getPiece(Location.fromCoordinates(col,row)));
            }
        }
    }
    
    @Test 
    void getPieceAndPutPiece() {
        game = new Game();
        Location location = Location.fromCoordinates(1, 1);
        game.putPiece(location, PieceType.King, Color.WHITE);
        assertEquals(Color.WHITE,game.getPiece(location).getColor());
        assertEquals(PieceType.King,game.getPiece(location).getType());
    }

    @Test
    void setupInitialBoard(){
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

        Location location;
        Piece piece;
        for (int col = 1; col <= 8; col++) {
            location = Location.fromCoordinates(col,1);
            piece = game.getPiece(location);
            assertEquals(Color.WHITE,game.getPiece(location).getColor());
            assertEquals(backRow[col-1],game.getPiece(location).getType());

            location = Location.fromCoordinates(col,2);
            piece = game.getPiece(location);
            assertEquals(Color.WHITE,game.getPiece(location).getColor());
            assertEquals(PieceType.Pawn,game.getPiece(location).getType());

            location = Location.fromCoordinates(col,7);
            piece = game.getPiece(location);
            assertEquals(Color.BLACK,game.getPiece(location).getColor());
            assertEquals(PieceType.Pawn,game.getPiece(location).getType());

            location = Location.fromCoordinates(col,8);
            piece = game.getPiece(location);
            assertEquals(Color.BLACK,game.getPiece(location).getColor());
            assertEquals(backRow[col-1],game.getPiece(location).getType());
        }
    }


}