package edu.hanover.sdp2026.core.useraction;

import edu.hanover.sdp2026.core.game.PieceType;

public final class PromotePawnAction implements UserAction {

    public final PieceType pieceType;

    public PromotePawnAction(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof PromotePawnAction)) return false;

        PromotePawnAction other = (PromotePawnAction) obj;
        return pieceType == other.pieceType;
    }
}
