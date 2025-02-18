package com.joshjs.gamangine.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.joshjs.gamangine.model.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DiscardCardEffect.class, name = "DiscardCardEffect"),
        @JsonSubTypes.Type(value = ModifyAttributeEffect.class, name = "ModifyAttributeEffect")
})
interface CardEffect {
    void applyEffect(GameState state, PlayerActionRequest action);
}
