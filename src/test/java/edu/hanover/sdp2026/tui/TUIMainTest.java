package edu.hanover.sdp2026.tui;

import static org.mockito.Mockito.*;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import edu.hanover.sdp2026.core.Interactor;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class TUIMainTest {
    @Test
    void startDrawsTitleAndExitsOnQ() throws Exception {
        Screen screen = mock(Screen.class);
        TextGraphics graphics = mock(TextGraphics.class, RETURNS_SELF);
        Interactor interactor = mock(Interactor.class);

        when(screen.newTextGraphics()).thenReturn(graphics);
        when(screen.readInput()).thenReturn(new KeyStroke('q', false, false));

        // Make sure close calls stopScreen, so implementation can use either
        doAnswer(invocation -> {
                    screen.stopScreen();
                    return null;
                })
                .when(screen)
                .close();

        TUIMain tui = new TUIMain(screen);
        tui.setInteractor(interactor);
        tui.start();

        InOrder order = inOrder(screen);
        order.verify(screen).startScreen();
        order.verify(screen).refresh();
        order.verify(screen).stopScreen();
        verify(graphics).putString(anyInt(), anyInt(), eq("Chess"));
        verifyNoInteractions(interactor);
    }
}
