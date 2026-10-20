package edu.hanover.sdp2026.core.game;

public class Move {
    private final Piece p;
    private final Location from;
    private final Location to;

    Move(Piece p, Location loc1, Location loc2) {
        this.p = p;
        from = loc1;
        to = loc2;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }
}
