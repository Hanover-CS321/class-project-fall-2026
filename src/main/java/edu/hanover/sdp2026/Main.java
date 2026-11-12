package edu.hanover.sdp2026;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import edu.hanover.sdp2026.core.GameManager;
import edu.hanover.sdp2026.tui.TUIMain;
import java.io.IOException;

public final class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        TUIMain tui = makeTUI();
        gameManager.setPresenter(tui);
        tui.setInteractor(gameManager);
        gameManager.start();
    }

    private static TUIMain makeTUI() {
        try {
            Terminal terminal = new DefaultTerminalFactory()
                    .setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.getDefaultOfSize(10))
                    .setInitialTerminalSize(new TerminalSize(80, 30))
                    .createTerminal();
            Screen screen = new TerminalScreen(terminal);
            return new TUIMain(screen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
