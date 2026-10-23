package edu.hanover.sdp2026.core;

import edu.hanover.sdp2026.core.useraction.UserAction;

public interface Interactor {
    void handleAction(UserAction action);
}
