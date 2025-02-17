package com.joshjs.gamangine.action;

import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.model.GameState;

import java.util.List;
import java.util.Map;

public class PlayCardAction implements GameAction {
    private final Map<String, Card> cards;

    public PlayCardAction(Map<String, Card> cards) {
        this.cards = cards;
    }

    @Override
    public void execute(GameState state, PlayerAction action) {
        String cardName = (String) action.actionData.get("cardName");
        List<Card> playerHand = state.playerHands.get(action.playerId);

        if (playerHand != null && playerHand.stream().anyMatch(card -> card.name.equals(cardName))) {
            Card card = cards.get(cardName);
            if (card != null) {
                card.applyEffects(state, action);
                playerHand.remove(card);
                state.discardPile.add(card);
            }
        } else {
            throw new IllegalStateException("Player does not have the specified card");
        }

    }
}
