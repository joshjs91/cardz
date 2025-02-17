package com.joshjs.gamangine.card;

import com.joshjs.gamangine.action.PlayerAction;
import com.joshjs.gamangine.model.GameState;

public class ModifyAttributeEffect implements CardEffect {
    @Override
    public void applyEffect(GameState state, PlayerAction action) {
        //TODO in general have lots to do with dealing with duplicate action inputs?????
        String targetPlayer = (String) action.actionData.get("modifyAttributeTargetPlayer");
        String attribute = (String) action.actionData.get("attributeToModify");
        //TODO some form of input as multiple minus plus there is a lot to do here. only handles ints at the moment
        int valueChange = (int) action.actionData.get("attributeToModifyValue");
        state.gameAttributes.put(attribute, (int) state.gameAttributes.getOrDefault(attribute, 0) + valueChange);
        System.out.println("Player " + targetPlayer + "'s attribute " + attribute + " changed by " + valueChange);
    }
}
