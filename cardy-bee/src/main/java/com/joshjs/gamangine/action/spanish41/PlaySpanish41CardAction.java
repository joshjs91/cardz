package com.joshjs.gamangine.action.spanish41;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.action.BaseAction;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41BaseEffect;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41DrawCardEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

import static com.joshjs.gamangine.action.validator.ActionValidator.validatePlayCardActionAndGetCardFromHand;

@EqualsAndHashCode(callSuper = true)
@JsonTypeName("PlaySpanish41CardAction")
@Data
//TODO need a much better way of dealing with multiple different type of card options....
//TODO should convert this to only accept a specific card Type i.e. concrete type
public class PlaySpanish41CardAction extends BaseAction {

    @JsonProperty("playedCard")
    private Card playedCard;

    @Override
    public Map<String, String> getRequiredInputs() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("playedCard", "Card");
        return inputs;
    }

    @Override
    public List<String> getFixedAttributes() {
        return new ArrayList<>();
    }

    @Override
    public void execute(GameState state, PlayerActionRequest action) {
        Card validCardInHand = validatePlayCardActionAndGetCardFromHand(state, playedCard,action);
        if (validCardInHand.getEffects().stream().noneMatch(effect -> effect.getClass().equals(Spanish41DrawCardEffect.class) || effect.getClass().equals(Spanish41BaseEffect.class))) {
            throw new InvalidInputException("You need to play a card that has either a colour or number value OR debuffs the required required draw card action!");
        }

        List<Card> playerHand = state.getPlayerHands().get(action.playerId);

        // Remove the valid card and apply effects
        playerHand.remove(validCardInHand);
        playedCard.applyEffects(state, action);
        state.getDiscardPile().add(playedCard);
    }
}
