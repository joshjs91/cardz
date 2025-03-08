package com.joshjs.gamangine.factory;

import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.action.DiscardCardAction;
import com.joshjs.gamangine.action.EndTurnAction;
import com.joshjs.gamangine.action.PlayCardAction;
import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.action.spanish41.PlaySpanish41CardAction;

import java.util.List;

public class GameFactory {


    public static List<Action> getChangeTurnActions(String gameType) {
        return switch (gameType) {
            case "spanish41"    -> spanish41TurnActions();
            case "complex"    -> complexTurnActions();
            default -> genericTurnActions();
        };
    }

    private static List<Action> genericTurnActions() {
        return List.of(
                new EndTurnAction(),
                new PlayCardAction(),
                new DiscardCardAction()
        );
    }

    private static List<Action> complexTurnActions() {
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(1);
        return List.of(
                new EndTurnAction(),
                new PlayCardAction(),
                drawCardAction,
                new DiscardCardAction()
        );
    }

    private static List<Action> spanish41TurnActions() {
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(1);
        return List.of(
                new PlaySpanish41CardAction(),
                drawCardAction
        );
    }

}