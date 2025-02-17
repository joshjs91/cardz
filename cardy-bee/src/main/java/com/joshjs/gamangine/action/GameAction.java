package com.joshjs.gamangine.action;

import com.joshjs.gamangine.model.GameState;

public interface GameAction {
    void execute(GameState state, PlayerAction action);
}
