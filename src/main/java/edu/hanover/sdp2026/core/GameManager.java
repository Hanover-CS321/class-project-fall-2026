package edu.hanover.sdp2026.core;

import edu.hanover.sdp2026.core.useraction.ChooseSquareAction;
import edu.hanover.sdp2026.core.useraction.NewGameAction;
import edu.hanover.sdp2026.core.useraction.PromotePawnAction;
import edu.hanover.sdp2026.core.useraction.ResignAction;
import edu.hanover.sdp2026.core.useraction.UndoAction;
import edu.hanover.sdp2026.core.useraction.UserAction;

public class GameManager implements Interactor {
    Presenter presenter;

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void start() {
        if (presenter == null) {
            throw new IllegalStateException(
                    "A presenter must be set before starting."
            );
        }
        presenter.start();
    }

    @Override
    public void handleAction(UserAction action) {
        if (action instanceof ChooseSquareAction) {
            handleChooseSquare((ChooseSquareAction) action);
        } else if (action instanceof PromotePawnAction) {
            handlePromotePawn((PromotePawnAction) action);
        } else if (action instanceof UndoAction) {
            handleUndo((UndoAction) action);
        } else if (action instanceof NewGameAction) {
            handleNewGame((NewGameAction) action);
        } else if (action instanceof ResignAction) {
            handleResign((ResignAction) action);
        } else {
            throw new IllegalArgumentException(
                    "Unsupported action: " + action
            );
        }
    }

    private void handleChooseSquare(ChooseSquareAction action) {
        throw new UnsupportedOperationException(
                "ChooseSquare processing not yet implemented."
        );
    }

    private void handlePromotePawn(PromotePawnAction action) {
        throw new UnsupportedOperationException(
                "PromotePawn processing not yet implemented."
        );
    }

    private void handleUndo(UndoAction action) {
        throw new UnsupportedOperationException(
                "Undo processing not yet implemented."
        );
    }

    private void handleNewGame(NewGameAction action) {
        throw new UnsupportedOperationException(
                "NewGame processing not yet implemented."
        );
    }

    private void handleResign(ResignAction action) {
        throw new UnsupportedOperationException(
                "Resign processing not yet implemented."
        );
    }
}
