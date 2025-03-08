package com.joshjs.gamangine.action.spanish41;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.action.BaseAction;
import com.joshjs.gamangine.action.PlayCardAction;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@JsonTypeName("DrawCardAction")
@Data
public class DrawCardAction extends BaseAction {

    @JsonProperty("cardsToDraw")
    private Integer cardsToDraw;

    @Override
    public Map<String, String> getRequiredInputs() {
        return new HashMap<>();
    }

    @Override
    public List<String> getFixedAttributes() {
        return new ArrayList<>(List.of("cardsToDraw"));
    }

    @Override
    public void execute(GameState state, PlayerActionRequest action) {
        state.drawCardsFromGameDeck(action.playerId, cardsToDraw);
        state.removePlayersActions(action.playerId);
        state.changeTurns();
        //TODO there should be a default set of actions somewhere the the turn ends
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(1);
        state.addActionsToPlayer(state.getCurrentPlayer(), List.of(new PlaySpanish41CardAction(), drawCardAction));
    }
}
