package com.joshjs.gamangine.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


@EqualsAndHashCode(callSuper = true)
@JsonTypeName("DiscardCardAction")
@Data
public class DiscardCardAction extends BaseAction {

    @JsonProperty("cardName")
    private String cardName;

    @JsonProperty("cardsToDiscard")
    private Integer cardsToDiscard;

    @Override
    public void execute(GameState state, PlayerActionRequest action) {
        if (cardName == null) {
            throw new IllegalArgumentException("Action input 'cardName' required for action");
        }
        List<Card> playerHand = state.getPlayerHands().get(action.playerId);
        Card discardedCard = removeIf(playerHand, card -> card.getName().equals(cardName));
        state.getDiscardPile().add(discardedCard);
    }

    @Override
    public Map<String, String> getRequiredInputs() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("cardName", "String");
        return inputs;
    }

    @Override
    public List<String> getFixedAttributes() {
        return new ArrayList<>(List.of("cardsToDiscard"));
    }

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
