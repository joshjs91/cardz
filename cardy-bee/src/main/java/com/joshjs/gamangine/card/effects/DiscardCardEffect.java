package com.joshjs.gamangine.card.effects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.action.model.PendingAction;
import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.action.DiscardCardAction;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeName("DiscardCardEffect")
@Data
public class DiscardCardEffect implements CardEffect {

    @JsonProperty("targetPlayer")
    private String targetPlayer;

    @Override
    public Map<String, String> getRequiredInputs() {
        HashMap<String, String> requiredInputs = new HashMap<>();
        requiredInputs.put("targetPlayer", "String");
        return requiredInputs;
    }

    @Override
    public List<String> getFixedAttributes() {
        return new ArrayList<>();
    }

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action) {
        System.out.println("Player " + targetPlayer + " must discard a card.");

        //TODO This would just be a reference to a set of actions - dont just create a new object here otherwise i need to set the reauired inputs in multiple places
        //TODO need to verify the targetPlayer can even satisfy the action i.e. they cant discard if they dont have any cards....
        Action newPendingPlayerAction = new DiscardCardAction();
        state.getPendingActions().add(new PendingAction(targetPlayer, newPendingPlayerAction));
        state.getPlayerAvailableActions().get(targetPlayer).add(newPendingPlayerAction);
    }
}
