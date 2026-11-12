package edu.hanover.sdp2026.core;

import edu.hanover.sdp2026.core.game.Location;
import edu.hanover.sdp2026.core.game.Move;
import java.util.List;

public interface Presenter {
    void start();

    void showPossibleMoves(Location selectedLocation, List<Move> possibleMoves);
}
