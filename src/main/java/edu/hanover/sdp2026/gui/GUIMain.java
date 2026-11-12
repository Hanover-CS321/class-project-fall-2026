package edu.hanover.sdp2026.gui;

import edu.hanover.sdp2026.core.Interactor;
import edu.hanover.sdp2026.core.Presenter;
import edu.hanover.sdp2026.core.game.Location;
import edu.hanover.sdp2026.core.game.Move;
import java.util.List;

/* Placeholder class for now */
public class GUIMain implements Presenter {
    private Interactor interactor;

    public void setInteractor(Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void start() {}

    @Override
    public void showPossibleMoves(Location selectedLocation, List<Move> possibleMoves) {
        // TODO: Highlight the selected square and possible destinations
        // When the board GUI is implemented.
    }
}
