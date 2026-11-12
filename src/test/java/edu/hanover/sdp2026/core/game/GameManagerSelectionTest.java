package edu.hanover.sdp2026.core.game;

import static org.junit.jupiter.api.Assertions.*;

import edu.hanover.sdp2026.core.GameManager;
import edu.hanover.sdp2026.core.Presenter;
import edu.hanover.sdp2026.core.useraction.ChooseSquareAction;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameManagerSelectionTest {
    private Game game;
    private GameManager manager;
    private TestPresenter presenter;

    @BeforeEach
    void setUp() {
        game = Game.createDefaultGame();
        manager = new GameManager(game);
        presenter = new TestPresenter();
        manager.setPresenter(presenter);
    }

    @Test
    void selectingOwnPawnDisplaysItsMoves() {
        manager.handleAction(new ChooseSquareAction(Location.fromString("a2")));

        assertEquals(Location.fromString("a2"), presenter.selectedLocation);
        assertNotNull(presenter.possibleMoves);
        assertEquals(
                List.of(Location.fromString("a3"), Location.fromString("a4")),
                presenter.possibleMoves.stream().map(Move::getTo).toList());
        assertEquals(1, presenter.displayCalls);
    }

    @Test
    void selectingAnotherOwnPieceReplacesSelection() {
        manager.handleAction(new ChooseSquareAction(Location.fromString("a2")));
        manager.handleAction(new ChooseSquareAction(Location.fromString("b2")));

        assertEquals(Location.fromString("b2"), presenter.selectedLocation);
        assertNotNull(presenter.possibleMoves);
        assertEquals(
                List.of(Location.fromString("b3"), Location.fromString("b4")),
                presenter.possibleMoves.stream().map(Move::getTo).toList());
        assertEquals(2, presenter.displayCalls);
    }

    @Test
    void selectingEmptySquareClearsSelection() {
        manager.handleAction(new ChooseSquareAction(Location.fromString("a2")));
        manager.handleAction(new ChooseSquareAction(Location.fromString("a3")));

        assertNull(presenter.selectedLocation);
        assertNotNull(presenter.possibleMoves);
        assertTrue(presenter.possibleMoves.isEmpty());
        assertEquals(2, presenter.displayCalls);
    }

    @Test
    void selectingOpposingPieceClearsSelection() {
        manager.handleAction(new ChooseSquareAction(Location.fromString("a2")));
        manager.handleAction(new ChooseSquareAction(Location.fromString("a7")));

        assertNull(presenter.selectedLocation);
        assertNotNull(presenter.possibleMoves);
        assertTrue(presenter.possibleMoves.isEmpty());
        assertEquals(2, presenter.displayCalls);
    }

    @Test
    void selectingBlockedOwnPieceKeepsItSelected() {
        manager.handleAction(new ChooseSquareAction(Location.fromString("a1")));

        assertEquals(Location.fromString("a1"), presenter.selectedLocation);
        assertNotNull(presenter.possibleMoves);
        assertTrue(presenter.possibleMoves.isEmpty());
        assertEquals(1, presenter.displayCalls);
    }

    @Test
    void selectingBlackPieceWorksOnBlackTurn() {
        game.setTurn(Color.BLACK);

        manager.handleAction(new ChooseSquareAction(Location.fromString("a7")));

        assertEquals(Location.fromString("a7"), presenter.selectedLocation);
        assertNotNull(presenter.possibleMoves);
        assertEquals(
                List.of(Location.fromString("a6"), Location.fromString("a5")),
                presenter.possibleMoves.stream().map(Move::getTo).toList());
        assertEquals(1, presenter.displayCalls);
    }

    @Test
    void selectingSquareWithoutPresenterThrows() {
        GameManager managerWithoutPresenter = new GameManager(game);

        assertThrows(
                IllegalStateException.class,
                () -> managerWithoutPresenter.handleAction(new ChooseSquareAction(Location.fromString("a2"))));
    }

    private static class TestPresenter implements Presenter {
        private Location selectedLocation;
        private List<Move> possibleMoves;
        private int displayCalls;

        @Override
        public void start() {}

        @Override
        public void showPossibleMoves(Location selectedLocation, List<Move> possibleMoves) {
            this.selectedLocation = selectedLocation;
            this.possibleMoves = possibleMoves;
            displayCalls++;
        }
    }
}
