package com.joshjs.gamangine.card;

import com.joshjs.gamangine.action.PlayerAction;
import com.joshjs.gamangine.model.GameState;

public class DiscardCardEffect implements CardEffect {
    @Override
    public void applyEffect(GameState state, PlayerAction action) {
        String targetPlayer = (String) action.actionData.get("discardCardTargetPlayer");

        System.out.println("Player " + targetPlayer + " must discard a card.");

        PlayerAction pendingAction = new PlayerAction();
        pendingAction.gameId = action.gameId;
        pendingAction.playerId = targetPlayer;
        pendingAction.actionType = "choose_discard";
        state.pendingActions.add(pendingAction);
    }
}
