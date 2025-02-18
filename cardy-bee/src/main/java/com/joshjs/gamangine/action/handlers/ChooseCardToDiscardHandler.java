package com.joshjs.gamangine.action.handlers;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.model.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;

import java.util.List;
import java.util.function.Predicate;

public class ChooseCardToDiscardHandler implements ActionHandler {

    @Override
    public void execute(GameState state, PlayerActionRequest action) {
        String cardName = (String) action.actionData.get("cardName");
        List<Card> playerHand = state.getPlayerHands().get(action.playerId);
        Card discardedCard = removeIf(playerHand, card -> card.name.equals(cardName));
        state.getDiscardPile().add(discardedCard);
    }

    //TODO this could be put into some card helper class
    public static <T> T removeIf(List<T> list, Predicate<T> condition) {
        for (T element : list) {
            if (condition.test(element)) {
                list.remove(element);
                return element;
            }
        }
        throw new IllegalStateException("User cant discard a card they don't have you fool");
    }
}
