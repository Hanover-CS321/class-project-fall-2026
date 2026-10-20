package edu.hanover.sdp2026.core.game;

public class Move {
    private final Piece piece;
    private final Location from;
    private final Location to;

    Move(Piece p, Location loc1, Location loc2) {
        this.piece = p;
        from = loc1;
        to = loc2;
    }

    public String moveAsString() {
        return piece.getType().getAbbr() + to.getStringEquivalent();
    }

    Piece getPiece() {
        return piece;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }
}
