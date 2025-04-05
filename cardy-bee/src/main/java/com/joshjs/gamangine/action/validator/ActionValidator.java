package com.joshjs.gamangine.action.validator;

import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;

import java.util.List;
import java.util.Map;

public class ActionValidator {

    /**
     * Validates that the exact card played exists in the player's hand.
     * Equality is based on the .equals() method of the Card class and its subclasses.
     */
    public static Card validatePlayCardActionAndGetCardFromHand(GameState state, Card playedCard, PlayerActionRequest action) {
        if (playedCard == null) {
            throw new InvalidInputException("Action input 'cardName' required for action.");
        }

        String playerId = action.getPlayerId();
        List<Card> playerHand = state.getPlayerHands().get(playerId);

        Card validCardInHand = playerHand.stream()
                .filter(card -> card.equals(playedCard))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Player does not have an exact matching card: " + playedCard.getName()));
        validateRequiredEffectInputs(playedCard, action);
        return validCardInHand;
    }

    /**
     * Validates that all required inputs declared by effects on the card
     * are present and non-blank in the action request.
     */
    public static void validateRequiredEffectInputs(Card card, PlayerActionRequest actionRequest) {
        for (CardEffect effect : card.getEffects()) {
            Map<String, String> requiredInputs = effect.getRequiredInputs();
            if (requiredInputs == null || requiredInputs.isEmpty()) continue;

            for (String key : requiredInputs.keySet()) {
                Object value = actionRequest.getInputs().get(key);
                if (value == null) {
                    throw new InvalidInputException("Missing required input: '" + key + "' for effect: " + effect.getClass().getSimpleName());
                }
            }
        }
    }
}
