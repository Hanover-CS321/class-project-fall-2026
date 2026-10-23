package edu.hanover.sdp2026.core.game;

import edu.hanover.sdp2026.core.GameManager;
import edu.hanover.sdp2026.core.Presenter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameManagerTest {

    private static class TestPresenter implements Presenter {
        private int startCalls;

        @Override
        public void start() {
            startCalls++;
        }
    }

    @Test
    void canStartGameManager() {
        GameManager gameManager = new GameManager();
        TestPresenter presenter = new TestPresenter();

        gameManager.setPresenter(presenter);
        gameManager.start();

        assertEquals(1, presenter.startCalls);
    }

    @Test
    void cannotStartWithoutPresenter() {
        GameManager gameManager = new GameManager();

        assertThrows(
                IllegalStateException.class, () -> gameManager.start()
        );
    }

    @Test
    void settingPresenterDoesNotStartIt() {
        GameManager gameManager = new GameManager();
        TestPresenter presenter = new TestPresenter();

        gameManager.setPresenter(presenter);

        assertEquals(0, presenter.startCalls);
    }
}