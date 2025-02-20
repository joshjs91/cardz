package com.joshjs.gamangine.action;

import com.joshjs.gamangine.action.handlers.ActionHandler;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static com.joshjs.gamangine.validator.UserInputValidator.verifyUserInput;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAction {
    public String actionType;
    public ActionHandler actionHandler;

    public void applyHandlers(GameState gameState, PlayerActionRequest action) {
        actionHandler.execute(gameState, action);
    }
}
