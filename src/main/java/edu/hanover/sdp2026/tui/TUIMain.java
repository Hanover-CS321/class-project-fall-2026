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
        boardRenderer.drawBoard(tg, geometry, Location.fromString("d1"), List.of());

        screen.refresh();
    }

    @Override
    public void showPossibleMoves(Location selectedLocation, List<Move> possibleMoves) {
        TextGraphics tg = screen.newTextGraphics();

        boardRenderer.drawBoard(tg, geometry, selectedLocation, possibleMoves);

        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
