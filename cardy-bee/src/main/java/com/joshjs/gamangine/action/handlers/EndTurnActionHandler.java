package com.joshjs.gamangine.action.handlers;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.action.PlayerAction;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.model.PlayerActionRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonTypeName("EndTurnActionHandler")
public class EndTurnActionHandler implements ActionHandler {

    @Override
    public void execute(GameState state, PlayerActionRequest action) {
        if (!state.getCurrentPlayer().equals(action.playerId)) {
            throw new IllegalStateException("Only the current player can end their turn");
        }
        updateTurn(state);
    }

    private void updateTurn(GameState state) {
        if (state.getPendingActions().isEmpty()) {
            int currentIndex = state.getPlayers().indexOf(state.getCurrentPlayer());
            state.setCurrentPlayer(state.getPlayers().get((currentIndex + 1) % state.getPlayers().size()));

            // Assign available actions for the new player
            //TODO there should be a default set of actions somewhere the the turn ends
            List<PlayerAction> newActions = List.of(
                    new PlayerAction("play_card", new HashMap<>(), List.of(new PlayCardActionHandler())),
                    new PlayerAction("end_turn", new HashMap<>(), List.of(new EndTurnActionHandler()))
            );
            state.getPlayerAvailableActions().put(state.getCurrentPlayer(), newActions);
            System.out.println("Turn changed to player: " + state.getCurrentPlayer());
        }
    }

}
