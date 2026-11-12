package edu.hanover.sdp2026.tui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import edu.hanover.sdp2026.core.game.Location;

public class BoardGeometry {
    private final int boardLeft; // actual board starts here
    private final int boardTop; // actual board starts here
    private final int squareCols; // width of each board cell
    private final int squareRows; // height of each board cell
    private final TerminalSize squareSize;

    public BoardGeometry(int boardLeft, int boardTop, int squareCols, int squareRows) {
        this.boardLeft = boardLeft;
        this.boardTop = boardTop;
        this.squareCols = squareCols;
        this.squareRows = squareRows;
        this.squareSize = new TerminalSize(squareCols, squareRows);
    }

    TerminalSize getSquareSize() {
        return squareSize;
    }

    TerminalPosition topLeftOf(Location loc) {
        return topLeftOf(loc.col(), loc.row());
    }

    private TerminalPosition topLeftOf(int col, int row) {
        return new TerminalPosition(boardLeft + squareCols * (col - 1), boardTop + squareRows * (8 - row));
    }

    TerminalPosition centerOf(Location location) {
        return centerOf(location.col(), location.row());
    }

    private TerminalPosition centerOf(int col, int row) {
        return new TerminalPosition(
                boardLeft + squareCols / 2 + squareCols * (col - 1),
                boardTop + squareRows / 2 + squareRows * (8 - row));
    }

    TerminalPosition labelForRow(int row) {
        return centerOf(1, row).withRelativeColumn(-squareCols);
    }

    TerminalPosition labelForCol(int col) {
        return centerOf(col, 1).withRelativeRow(squareRows);
    }
}
