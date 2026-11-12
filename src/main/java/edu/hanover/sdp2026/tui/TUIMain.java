package edu.hanover.sdp2026.tui;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import edu.hanover.sdp2026.core.Interactor;
import edu.hanover.sdp2026.core.Presenter;
import edu.hanover.sdp2026.core.game.Location;
import edu.hanover.sdp2026.core.game.Move;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TUIMain implements Presenter {
    private final Screen screen;
    private final BoardRenderer boardRenderer = new BoardRenderer();
    private Interactor interactor;
    private BoardGeometry geometry;

    public TUIMain(Screen screen) {
        this.screen = screen;
    }

    public void setInteractor(Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void start() {
        try (screen) {
            initialize();
            initialDraw();
            mainLoop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialize() throws IOException {
        screen.startScreen();
        geometry = new BoardGeometry(4, 2, 5, 3);
    }

    private void mainLoop() throws IOException {
        while (true) {
            KeyStroke input = screen.readInput();
            if (input.getKeyType().equals(KeyType.Character)
                    && input.getCharacter().equals('q')) break;
        }
    }

    private void initialDraw() throws IOException {
        TextGraphics tg = screen.newTextGraphics();
        tg.putString(0, 0, "Chess");
        boardRenderer.drawBoard(tg, geometry);

        screen.refresh();
    }

    @Override
    public void showPossibleMoves(Location selectedLocation, List<Move> possibleMoves) {
        String message;

        if (selectedLocation == null) {
            message = "Selection cleared.";
        } else if (possibleMoves.isEmpty()) {
            message = "Selected: " + selectedLocation.getStringEquivalent() + " | No possible moves.";
        } else {
            String destinations = possibleMoves.stream()
                    .map(move -> move.getTo().getStringEquivalent())
                    .collect(Collectors.joining(", "));

            message = "Selected: " + selectedLocation.getStringEquivalent() + " | Moves: " + destinations;
        }

        int width = screen.getTerminalSize().getColumns();
        if (width <= 0) {
            return;
        }

        String visibleMessage = message.substring(0, Math.min(message.length(), width));
        TextGraphics tg = screen.newTextGraphics();
        tg.putString(0, 1, " ".repeat(width));
        tg.putString(0, 1, visibleMessage);

        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
