package edu.hanover.sdp2026.tui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import edu.hanover.sdp2026.core.game.Color;
import edu.hanover.sdp2026.core.game.Location;
import edu.hanover.sdp2026.core.game.Piece;
import edu.hanover.sdp2026.core.game.PieceType;

public class BoardRenderer {
    private static final TextColor LIGHT_SQUARE = TextColor.Factory.fromString("#f0d9b5"); // cream
    private static final TextColor DARK_SQUARE = TextColor.Factory.fromString("#b58863"); // brown
    private static final TextColor WHITE_PIECE = TextColor.Factory.fromString("#ffffff");
    private static final TextColor BLACK_PIECE = TextColor.Factory.fromString("#1a1a1a");
    private static final TextColor SELECTED = TextColor.Factory.fromString("#f6f669"); // yellow
    private static final TextColor LEGAL_MOVE = TextColor.Factory.fromString("#6b8e23"); // olive

    void drawBoard(TextGraphics tg, BoardGeometry geometry) {
        drawLabels(tg, geometry);
        clearBoard(tg, geometry);
        drawPieces(tg, geometry);
    }

    void clearBoard(TextGraphics tg, BoardGeometry geometry) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                clearSquare(tg, Location.fromCoordinates(i, j), geometry);
            }
        }
    }

    void clearSquare(TextGraphics tg, Location loc, BoardGeometry geometry) {
        TextColor bgColor = getSquareColor(loc);
        TerminalPosition topLeft = geometry.topLeftOf(loc);
        tg.setBackgroundColor(bgColor)
                .setForegroundColor(LIGHT_SQUARE)
                .fillRectangle(topLeft, geometry.getSquareSize(), ' ');
    }

    void drawLabels(TextGraphics tg, BoardGeometry geometry) {
        for (int i = 1; i <= 8; i++) {
            tg.putString(geometry.labelForRow(i), String.valueOf(i));
        }
        for (int i = 1; i <= 8; i++) {
            tg.putString(geometry.labelForCol(i), String.valueOf((char) ('a' + i - 1)));
        }
    }

    void drawPieces(TextGraphics tg, BoardGeometry geometry) {
        // Demo for now, not long-term viable
        drawPiece(tg, geometry, new Piece(PieceType.Queen, Color.WHITE), Location.fromString("d1"));
        drawPiece(tg, geometry, new Piece(PieceType.King, Color.WHITE), Location.fromString("e1"));
        drawPiece(tg, geometry, new Piece(PieceType.Queen, Color.BLACK), Location.fromString("d8"));
        drawPiece(tg, geometry, new Piece(PieceType.King, Color.BLACK), Location.fromString("e8"));
    }

    void drawPiece(TextGraphics tg, BoardGeometry geometry, Piece piece, Location location) {
        TextColor pieceColor = getPieceColor(piece);
        TextColor squareColor = getSquareColor(location);
        String pieceString = getPieceString(piece);
        tg.setBackgroundColor(squareColor)
                .setForegroundColor(pieceColor)
                .putString(geometry.centerOf(location), pieceString);
    }

    private TextColor getPieceColor(Piece piece) {
        return piece.getColor().equals(Color.WHITE) ? WHITE_PIECE : BLACK_PIECE;
    }

    private TextColor getSquareColor(Location loc) {
        return (loc.col() + loc.row()) % 2 == 0 ? DARK_SQUARE : LIGHT_SQUARE;
    }

    public static String getPieceString(Piece piece) {
        return switch (piece.getType()) {
            case Pawn -> "♟";
            case Rook -> "♜";
            case Knight -> "♞";
            case Bishop -> "♝";
            case Queen -> "♛";
            case King -> "♚";
        };
    }
}
