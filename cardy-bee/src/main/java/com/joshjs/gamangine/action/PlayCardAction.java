package com.joshjs.gamangine.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JsonTypeName("PlayCardAction")
@Data
public class PlayCardAction implements Action {

    @JsonProperty("card")
    private Card playedCard;

    @Override
    public Map<String, String> getRequiredInputs() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("playedCard", "Card");
        return inputs;
    }

    @Override
    public void execute(GameState state, PlayerActionRequest action) {
        if (playedCard == null) {
            throw new IllegalArgumentException("Action input 'cardName' required for action");
        }
        List<Card> playerHand = state.getPlayerHands().get(action.playerId);
        Optional<Card> cardOpt = playerHand.stream().filter(cardInHand -> cardInHand.equals(playedCard)).findFirst();
        if (cardOpt.isPresent()) {
            Card cardInHand = cardOpt.get();
            playerHand.remove(cardInHand);
            //TODO ADD TEST FOR THIS.....MAKE SURE ITS RUNNING PLAYED CARD AND REMOVING CARD ETC. 
            playedCard.applyEffects(state, action);
            state.getDiscardPile().add(cardInHand);
        } else {
            throw new IllegalStateException("User cant play a card they don't have you fool");
        }
    }


}
