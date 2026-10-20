package edu.hanover.sdp2026.core.game;

public enum PieceType {
    Pawn(""),
    Rook("R"),
    Knight("N"),
    Bishop("B"),
    Queen("Q"),
    King("K");

    private final String abbr;

    PieceType(String abbr) {
        this.abbr = abbr;
    }

    String getAbbr() {
        return this.abbr;
    }
}
