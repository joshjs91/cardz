package com.joshjs.gamangine.validator;

import com.joshjs.gamangine.model.PlayerActionRequest;

import java.util.Map;

public class UserInputValidator {

    public static void verifyUserInput(Map<String, Class<?>> requiredInputs, PlayerActionRequest action) {
        for (String requiredInputKey : requiredInputs.keySet()) {
            Object actionInputValue = action.actionData.get(requiredInputKey);
            if (actionInputValue == null) {
                throw new RuntimeException("Missing input!: " + requiredInputKey);
            }
            if (!actionInputValue.getClass().equals(requiredInputs.get(requiredInputKey))) {
                throw new RuntimeException("invalid input!: " + requiredInputKey);
            }
        }
    }
}
