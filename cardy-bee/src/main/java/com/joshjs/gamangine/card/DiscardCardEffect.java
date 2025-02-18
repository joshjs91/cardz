package com.joshjs.gamangine.card;

import com.joshjs.gamangine.action.PendingAction;
import com.joshjs.gamangine.action.handlers.ChooseCardToDiscardHandler;
import com.joshjs.gamangine.action.PlayerAction;
import com.joshjs.gamangine.model.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;

import java.util.HashMap;
import java.util.List;

public class DiscardCardEffect implements CardEffect {

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action) {
        String targetPlayer = (String) action.actionData.get("discardCardTargetPlayer");
        System.out.println("Player " + targetPlayer + " must discard a card.");

        //TODO This would just be a reference to a set of actions - dont just create a new object here otherwise i need to set the reauired inputs in multiple places
        PlayerAction pendingAction = new PlayerAction("choose_discard", new HashMap<>(), List.of(new ChooseCardToDiscardHandler()));
        state.getPendingActions().add(new PendingAction(targetPlayer, pendingAction));
    }
}
