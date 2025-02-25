package com.joshjs.gamangine.game;

import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.action.DiscardCardAction;
import com.joshjs.gamangine.action.EndTurnAction;
import com.joshjs.gamangine.action.PlayCardAction;

import java.util.List;

public class GameConfig {

    public static List<Action> genericTurnActions() {
        EndTurnAction endTurnAction = new EndTurnAction();
        endTurnAction.setIsRequired(true);

        return List.of(
                endTurnAction,
                new PlayCardAction(),
                new DiscardCardAction()
        );
    }

}