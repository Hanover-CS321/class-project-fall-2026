package edu.hanover.sdp2026.tui;

import edu.hanover.sdp2026.core.Interactor;
import edu.hanover.sdp2026.core.useraction.UserAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TUIMainTest {
    @Test
    void testStartCallsInteractorStart() {
        TUIMain main = new TUIMain();
        InteractorSpy interactor = new InteractorSpy();

        main.setInteractor(interactor);
        main.start();
        assertEquals(1, interactor.timesCalled);
    }

    private static class InteractorSpy implements Interactor {
        public int timesCalled = 0;

        @Override
        public void handleAction(UserAction action) {

        }

        public void start() {
            timesCalled += 1;
        }

    }
}