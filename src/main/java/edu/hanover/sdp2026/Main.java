package edu.hanover.sdp2026;

import edu.hanover.sdp2026.core.GameManager;
import edu.hanover.sdp2026.tui.TUIMain;

public final class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        TUIMain tui = new TUIMain();
        gameManager.setPresenter(tui);
        tui.setInteractor(gameManager);
        tui.start();
    }
}