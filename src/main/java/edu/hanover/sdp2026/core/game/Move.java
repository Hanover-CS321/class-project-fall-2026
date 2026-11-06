package edu.hanover.sdp2026.core.game;

public class Move {
    private final Piece piece;
    private final Location from;
    private final Location to;

    Move(Piece piece, Location from, Location to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
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
