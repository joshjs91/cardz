package com.joshjs.gamangine.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.joshjs.gamangine.action.validator.ActionValidator.validatePlayedCard;

@EqualsAndHashCode(callSuper = true)
@JsonTypeName("PlayCardAction")
@Data
public class PlayCardAction extends BaseAction {

    @JsonProperty("card")
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
        Card validCardInHand = validatePlayedCard(state, playedCard, action.playerId);
        List<Card> playerHand = state.getPlayerHands().get(action.playerId);

        // Remove the valid card and apply effects
        playerHand.remove(validCardInHand);
        playedCard.applyEffects(state, action);
        state.getDiscardPile().add(validCardInHand);
        System.out.println();
    }
}
