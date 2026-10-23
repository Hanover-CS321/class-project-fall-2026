package edu.hanover.sdp2026.core.useraction;

import edu.hanover.sdp2026.core.game.Location;

public final class ChooseSquareAction implements UserAction {

    public final Location location;

    public ChooseSquareAction(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof ChooseSquareAction)) return false;

        ChooseSquareAction other = (ChooseSquareAction) obj;
        return location.equals(other.location);
    }
}
