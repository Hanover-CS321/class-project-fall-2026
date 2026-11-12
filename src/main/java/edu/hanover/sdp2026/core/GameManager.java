package edu.hanover.sdp2026.core;

import edu.hanover.sdp2026.core.game.Game;
import edu.hanover.sdp2026.core.game.Location;
import edu.hanover.sdp2026.core.game.Move;
import edu.hanover.sdp2026.core.useraction.ChooseSquareAction;
import edu.hanover.sdp2026.core.useraction.NewGameAction;
import edu.hanover.sdp2026.core.useraction.PromotePawnAction;
import edu.hanover.sdp2026.core.useraction.ResignAction;
import edu.hanover.sdp2026.core.useraction.UndoAction;
import edu.hanover.sdp2026.core.useraction.UserAction;
import java.util.List;
import java.util.Objects;

public class GameManager implements Interactor {
    Presenter presenter;

    private final Game game;
    private Location selectedLocation;

    public GameManager() {
        this(Game.createDefaultGame());
    }

    public GameManager(Game game) {
        this.game = Objects.requireNonNull(game);
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void start() {
        if (presenter == null) {
            throw new IllegalStateException("A presenter must be set before starting.");
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
            throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }

    private void handleChooseSquare(ChooseSquareAction action) {
        if (presenter == null) {
            throw new IllegalStateException("A presenter must be set before selecting a square.");
        }

        List<Move> possibleMoves = game.computeMoves(action.location);

        if (possibleMoves == null) {
            selectedLocation = null;
            presenter.showPossibleMoves(null, List.of());
            return;
        }

        selectedLocation = action.location;
        presenter.showPossibleMoves(selectedLocation, possibleMoves);
    }

    private void handlePromotePawn(PromotePawnAction action) {
        throw new UnsupportedOperationException("PromotePawn processing not yet implemented.");
    }

    private void handleUndo(UndoAction action) {
        throw new UnsupportedOperationException("Undo processing not yet implemented.");
    }

    private void handleNewGame(NewGameAction action) {
        throw new UnsupportedOperationException("NewGame processing not yet implemented.");
    }

    private void handleResign(ResignAction action) {
        throw new UnsupportedOperationException("Resign processing not yet implemented.");
    }
}
