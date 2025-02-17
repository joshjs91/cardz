package com.joshjs.gamangine.action;

import com.joshjs.gamangine.model.GameState;

import java.util.ArrayList;
import java.util.List;

public class EndTurnAction implements GameAction {
    @Override
    public void execute(GameState state, PlayerAction action) {
        if (!state.currentPlayer.equals(action.playerId)) {
            throw new IllegalStateException("Only the current player can end their turn");
        }

        updateTurn(state);
    }

    private void updateTurn(GameState state) {
        if (state.pendingActions.isEmpty()) {
            int currentIndex = state.players.indexOf(state.currentPlayer);
            state.currentPlayer = state.players.get((currentIndex + 1) % state.players.size());

            // Assign available actions for the new player
            state.availableActions = List.of("play_card", "end_turn", "use_skill");
            state.playerAvailableActions.put(state.currentPlayer, new ArrayList<>(state.availableActions));

            System.out.println("Turn changed to player: " + state.currentPlayer);
        }
    }

}
