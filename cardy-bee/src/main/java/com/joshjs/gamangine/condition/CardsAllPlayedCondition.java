package com.joshjs.gamangine.condition;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.state.GameState;

import java.util.List;

@JsonTypeName("CardsAllPlayedCondition")
public class CardsAllPlayedCondition implements Condition {
    @Override
    public boolean evaluate(GameState state) {
        return state.getPlayerHands().values().stream().allMatch(List::isEmpty);
    }
}
