package edu.hanover.sdp2026.tui;

import edu.hanover.sdp2026.core.Interactor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TUIMainTest {
    @Test
    void testStartCallsInteractorStart() {
        TUIMain main = new TUIMain();
        Interactor interactor = mock(Interactor.class);

        main.setInteractor(interactor);
        main.start();
        verify(interactor, times(1)).start();
    }

}