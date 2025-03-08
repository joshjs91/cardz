package com.joshjs.gamangine.action.validator;

import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.state.GameState;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ActionValidator {

    public static Card validatePlayedCard(GameState state, Card playedCard, String player) {
        if (playedCard == null) {
            throw new InvalidInputException("Action input 'cardName' required for action");
        }
        List<Card> playerHand = state.getPlayerHands().get(player);
        return playerHand.stream()
                .filter(card -> {
                    if (!card.getName().equals(playedCard.getName())) return false;
                    if (!cardsHaveSameEffectClasses(card, playedCard)) return false;
                    if (!cardsHaveSameFixedAttributes(card, playedCard)) return false;
                    return true;
                })
                .findFirst()
                .orElseThrow(() -> determineError(playerHand, playedCard));
    }

    private static RuntimeException determineError(List<Card> playerHand, Card playedCard) {
        InvalidInputException lastError = new InvalidInputException("Player does not have a valid card named: " + playedCard.getName());

        for (Card card : playerHand) {
            if (!card.getName().equals(playedCard.getName())) {
                continue; // Try next card
            }
            lastError = new InvalidInputException("Player has card '" + playedCard.getName() + "' but with different effect types.");
            if (!cardsHaveSameEffectClasses(card, playedCard)) {
                continue; // Try next card
            }
            lastError = new InvalidInputException("Effect attributes were tampered with for card: " + playedCard.getName());
            if (!cardsHaveSameFixedAttributes(card, playedCard)) {
                continue; // Try next card
            }
            return new RuntimeException("No idea what has happened here this should never happen");
        }

        return lastError; // If no card passed all checks, return the last encountered error
    }

    private static boolean cardsHaveSameEffectClasses(Card card1, Card card2) {
        List<Class<?>> card1EffectClasses = card1.getEffects().stream()
                .map(CardEffect::getClass)
                .collect(Collectors.toList());

        List<Class<?>> card2EffectClasses = card2.getEffects().stream()
                .map(CardEffect::getClass)
                .collect(Collectors.toList());

        return card1EffectClasses.equals(card2EffectClasses);
    }

    private static boolean cardsHaveSameFixedAttributes(Card card1, Card card2) {
        for (int i = 0; i < card1.getEffects().size(); i++) {
            CardEffect effect1 = card1.getEffects().get(i);
            CardEffect effect2 = card2.getEffects().get(i);

            for (String fixedAttr : effect1.getFixedAttributes()) {
                Object value1 = getFieldValue(effect1, fixedAttr);
                Object value2 = getFieldValue(effect2, fixedAttr);

                if (!Objects.equals(value1, value2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Object getFieldValue(CardEffect effect, String fieldName) {
        try {
            Field field = effect.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(effect);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error reading field '" + fieldName + "' from effect: " + effect.getClass().getSimpleName(), e);
        }
    }
}
