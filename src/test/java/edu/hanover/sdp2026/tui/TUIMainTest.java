package edu.hanover.sdp2026.tui;

import static org.mockito.Mockito.*;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import edu.hanover.sdp2026.core.Interactor;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class TUIMainTest {
    @Test
    void startDrawsTitleAndExitsOnQThenEnter() throws Exception {
        Screen screen = mock(Screen.class);
        TextGraphics graphics = mock(TextGraphics.class, RETURNS_SELF);
        Interactor interactor = mock(Interactor.class);

        when(screen.newTextGraphics()).thenReturn(graphics);
        when(screen.getTerminalSize()).thenReturn(new TerminalSize(100, 35));
        when(screen.readInput())
                .thenReturn(new KeyStroke('q', false, false), new KeyStroke(KeyType.Enter), new KeyStroke(KeyType.EOF));

        TUIMain tui = new TUIMain(screen);
        tui.setInteractor(interactor);
        tui.start();

        InOrder order = inOrder(screen);
        order.verify(screen).startScreen();
        order.verify(screen, atLeastOnce()).refresh();
        order.verify(screen).close();

        verify(screen, times(2)).readInput();
        verify(graphics, atLeastOnce()).putString(0, 0, "Chess");
        verifyNoInteractions(interactor);
    }
}
