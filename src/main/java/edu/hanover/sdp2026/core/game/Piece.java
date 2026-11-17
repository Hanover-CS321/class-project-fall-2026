package edu.hanover.sdp2026.core.game;

import java.util.Objects;

public class Piece {
    private final PieceType type;
    private final Color color;

    public Piece(PieceType type, Color color) {
        this.type = type;
        this.color = color;
    }

    public PieceType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return color + " " + type;
    }

    // equals() is intended to act as is
    @Override
    public boolean equals(Object piece) {
        return this == piece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }
}
