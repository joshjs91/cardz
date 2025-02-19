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
    public Map<String, Class<?>> requiredInputs;
    public List<ActionHandler> actionHandlers;

    public void applyHandlers(GameState gameState, PlayerActionRequest action) {
        verifyUserInput(requiredInputs, action);
        for (ActionHandler actionHandler : actionHandlers) {
            actionHandler.execute(gameState, action);
        }
    }
}
