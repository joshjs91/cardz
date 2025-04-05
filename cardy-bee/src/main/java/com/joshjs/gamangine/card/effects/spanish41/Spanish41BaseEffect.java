package com.joshjs.gamangine.card.effects.spanish41;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.action.spanish41.PlaySpanish41CardAction;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.NumberAndColourCard;
import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.*;

import static com.joshjs.gamangine.card.effects.EffectHelpers.getLastPlayedCard;

@JsonTypeName("Spanish41BaseEffect")
@Data
public class Spanish41BaseEffect implements CardEffect {

    @Override
    public Map<String, String> getRequiredInputs() {
        return new HashMap<>();
    }

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action, Card card) {
        Optional<Card> lastPlayedCardOpt = getLastPlayedCard(state);

        if (lastPlayedCardOpt.isPresent()) {
            Card lastCard = lastPlayedCardOpt.get();

            boolean hasMatchingNumber = compareAttribute(lastCard, card, "getNumber");
            boolean hasMatchingColour = compareAttribute(lastCard, card, "getColour");
            boolean hasRelevantAttributes = hasMethod(lastCard, "getNumber") || hasMethod(lastCard, "getColour");

            if (!hasRelevantAttributes) {
                throw new InvalidInputException("Cannot play a numbered and coloured card on a card that does not have number or colour.");
            }

            if (!hasMatchingNumber && !hasMatchingColour) {
                throw new InvalidInputException("You must play a card that matches either the color or number!");
            }
        }

        changeTurnAndSetActions(state, action.playerId);
    }



    private boolean compareAttribute(Card lastCard, Card playedCard, String methodName) {
        try {
            Method lastMethod = lastCard.getClass().getMethod(methodName);
            Method playedMethod = playedCard.getClass().getMethod(methodName);

            Object lastValue = lastMethod.invoke(lastCard);
            Object playedValue = playedMethod.invoke(playedCard);

            return lastValue != null && lastValue.equals(playedValue);
        } catch (NoSuchMethodException e) {
            return false; // One or both cards don't have the method
        } catch (Exception e) {
            throw new InternalError("Error comparing attribute: " + methodName + " - " + e.getMessage());
        }
    }

    private boolean hasMethod(Card card, String methodName) {
        try {
            card.getClass().getMethod(methodName);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }


    private void changeTurnAndSetActions(GameState state, String player) {
        state.removePlayersActions(player);
        state.changeTurns();
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(1);
        state.addActionsToPlayer(state.getCurrentPlayer(), List.of(new PlaySpanish41CardAction(), drawCardAction));
    }
}
