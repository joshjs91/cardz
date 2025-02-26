package com.joshjs.gamangine.action;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
        updateTurn(state);
    }

    @Override
    public Map<String, String> getRequiredInputs() {
        return new HashMap<>();
    }

    private void updateTurn(GameState state) {
        int currentIndex = state.getPlayers().indexOf(state.getCurrentPlayer());
        state.setCurrentPlayer(state.getPlayers().get((currentIndex + 1) % state.getPlayers().size()));

        // Assign available actions for the new player
        //TODO there should be a default set of actions somewhere the the turn ends
        List<Action> newActions = List.of(new PlayCardAction(), new EndTurnAction());
        state.getPlayerAvailableActions().put(state.getCurrentPlayer(), newActions);
        System.out.println("Turn changed to player: " + state.getCurrentPlayer());
    }
}
