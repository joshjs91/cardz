package com.joshjs.gamangine.card.effects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeName("ModifyPlayerNumberAttributeEffect")
@Data
public class UselessEffect implements CardEffect {

    @Override
    public Map<String, String> getRequiredInputs() {
        return  new HashMap<>();
    }

    @Override
    public List<String> getFixedAttributes() {
        return List.of();
    }

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action) {

    }
}
