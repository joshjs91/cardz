package com.joshjs.gamangine.condition;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.state.GameState;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeName("CardsAllPlayedCondition")
@Data
public class CardsAllPlayedCondition implements Condition {

    @Override
    public Map<String, String> getRequiredInputs() {
        return new HashMap<>();
    }

    @Override
    public boolean evaluate(GameState state) {
        return state.getPlayerHands().values().stream().allMatch(List::isEmpty);
    }
}
