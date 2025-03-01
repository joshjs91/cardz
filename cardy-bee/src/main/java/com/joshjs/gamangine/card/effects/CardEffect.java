package com.joshjs.gamangine.card.effects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DiscardCardEffect.class, name = "DiscardCardEffect"),
        @JsonSubTypes.Type(value = ModifyGameNumberAttributeEffect.class, name = "ModifyPlayerNumberAttributeEffect")
})
public interface CardEffect {
    Map<String, String> getRequiredInputs();
    List<String> getFixedAttributes();
    void applyEffect(GameState state, PlayerActionRequest action);
}
