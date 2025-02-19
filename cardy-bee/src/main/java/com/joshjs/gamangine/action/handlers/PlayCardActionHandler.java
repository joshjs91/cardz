package com.joshjs.gamangine.action.handlers;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;

import java.util.List;
import java.util.Optional;

@JsonTypeName("PlayCardActionHandler")
public class PlayCardActionHandler implements ActionHandler {

    @Override
    public void execute(GameState state, PlayerActionRequest action) {
        String cardName = (String) action.actionData.get("cardName");
        List<Card> playerHand = state.getPlayerHands().get(action.playerId);
        Optional<Card> cardOpt = playerHand.stream().filter(card -> card.getName().equals(cardName)).findFirst();
        if (cardOpt.isPresent()) {
            Card card = cardOpt.get();
            card.applyEffects(state, action);
            playerHand.remove(card);
            state.getDiscardPile().add(card);
        } else {
            throw new IllegalStateException("Player does not have the specified card");
        }

    }
}
