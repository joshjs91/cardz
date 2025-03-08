package com.joshjs.gamangine.action;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@JsonTypeName("EndTurnAction")
@Data
public class EndTurnAction extends BaseAction {

    @Override
    public void execute(GameState state, PlayerActionRequest action) {
        if (!state.getCurrentPlayer().equals(action.playerId)) {
            throw new IllegalStateException("Only the current player can end their turn");
        }
        state.removePlayersActions(action.playerId);
        state.changeTurns();
        //TODO there should be a default set of actions somewhere the the turn ends
        List<Action> changeInTurnActions = new ArrayList<>(List.of(new PlayCardAction(), new EndTurnAction()));
        state.addActionsToPlayer(state.getCurrentPlayer(), changeInTurnActions);
    }

    @Override
    public Map<String, String> getRequiredInputs() {
        return new HashMap<>();
    }

    @Override
    public List<String> getFixedAttributes() {
        return new ArrayList<>();
    }
}
