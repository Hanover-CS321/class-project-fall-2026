package edu.hanover.sdp2026.tui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import edu.hanover.sdp2026.core.Interactor;
import edu.hanover.sdp2026.core.Presenter;
import edu.hanover.sdp2026.core.game.Location;
import edu.hanover.sdp2026.core.game.Move;
import edu.hanover.sdp2026.core.useraction.ChooseSquareAction;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TUIMain implements Presenter {
    private static final int MAX_INPUT_LENGTH = 40;

    private final Screen screen;
    private final BoardRenderer boardRenderer = new BoardRenderer();
    private final StringBuilder inputBuffer = new StringBuilder();

    private Interactor interactor;
    private BoardGeometry geometry;

    private Location selectedLocation;
    private List<Move> possibleMoves = List.of();

    private String statusMessage = "Type a square such as d2 and press Enter to select a piece.";

    public TUIMain(Screen screen) {
        this.screen = screen;
    }

    public void setInteractor(Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void start() {
        if (interactor == null) {
            throw new IllegalStateException("An interactor must be set before starting.");
        }

        try (screen) {
            initialize();
            redraw();
            mainLoop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialize() throws IOException {
        screen.startScreen();

        // Leave room above the board for instructions and input.
        geometry = new BoardGeometry(4, 5, 5, 3);

        screen.setCursorPosition(null);
    }

    private void mainLoop() throws IOException {
        while (true) {
            KeyStroke input = screen.readInput();

            if (input == null) continue;

            if (input.getKeyType() == KeyType.EOF) break;

            if (input.getKeyType() == KeyType.Enter) {
                if (!submitInput()) break;
            } else if (input.getKeyType() == KeyType.Backspace) {
                removeLastCharacter();
            } else if (input.getKeyType() == KeyType.Character) {
                appendCharacter(input);
            }

            redraw();
        }
    }

    private void appendCharacter(KeyStroke input) {
        Character character = input.getCharacter();

        if (character != null && !Character.isISOControl(character) && inputBuffer.length() < MAX_INPUT_LENGTH) {
            inputBuffer.append(character);
        }
    }

    private void removeLastCharacter() {
        if (!inputBuffer.isEmpty()) inputBuffer.deleteCharAt(inputBuffer.length() - 1);
    }

    private boolean submitInput() {
        String command = inputBuffer.toString().trim().toLowerCase(Locale.ROOT);

        inputBuffer.setLength(0);

        if (command.equals("q") || command.equals("quit")) return false;

        if (command.isEmpty()) {
            statusMessage = "Enter a square, such as d2.";
            return true;
        }

        if (!command.matches("[a-h][1-8]")) {
            statusMessage = "Invalid square. Use a-h and 1-8, such as d2.";
            return true;
        }

        try {
            interactor.handleAction(new ChooseSquareAction(Location.fromString(command)));
        } catch (UnsupportedOperationException e) {
            statusMessage = "Move generation for this piece is not available yet. " + "Choose another piece.";
        }

        return true;
    }

    private void redraw() throws IOException {
        screen.clear();

        TextGraphics tg = screen.newTextGraphics();

        drawTextLine(tg, 0, "Chess");
        drawTextLine(tg, 1, "Type a square + Enter | q or quit + Enter: exit");
        drawTextLine(tg, 2, statusMessage);
        drawTextLine(tg, 3, "Select a square: " + inputBuffer);

        tg.setBackgroundColor(TextColor.ANSI.DEFAULT).setForegroundColor(TextColor.ANSI.DEFAULT);

        boardRenderer.drawBoard(tg, geometry, selectedLocation, possibleMoves);

        screen.refresh();
    }

    private void drawTextLine(TextGraphics tg, int row, String text) {
        int width = screen.getTerminalSize().getColumns();
        int height = screen.getTerminalSize().getRows();

        if (width <= 0 || row >= height) return;

        String visibleText = text.length() > width ? text.substring(0, width) : text;

        tg.setBackgroundColor(TextColor.ANSI.DEFAULT)
                .setForegroundColor(TextColor.ANSI.DEFAULT)
                .putString(0, row, visibleText);
    }

    @Override
    public void showPossibleMoves(Location selectedLocation, List<Move> possibleMoves) {
        this.selectedLocation = selectedLocation;
        this.possibleMoves = possibleMoves;

        if (selectedLocation == null) {
            statusMessage = "No selectable piece there. Choose one of your pieces.";
        } else if (possibleMoves.isEmpty()) {
            statusMessage = "Selected square has no available destinations.";
        } else {
            statusMessage = "Piece selected. " + possibleMoves.size() + " possible destinations.";
        }

        try {
            redraw();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
