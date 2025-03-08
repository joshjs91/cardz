package com.joshjs.gamangine.card.effects;

import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.model.state.GameState;

import java.util.Optional;

public class EffectHelpers {

    /**
     * Gets the last played card from the discard pile.
     */
    public static Optional<Card> getLastPlayedCard(GameState state) {
        if (state.getDiscardPile().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(state.getDiscardPile().get(state.getDiscardPile().size() - 1));
    }
}
