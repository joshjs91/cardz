package com.joshjs.gamangine.card.effects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41BaseEffect;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41DrawCardEffect;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DiscardCardEffect.class, name = "DiscardCardEffect"),
        @JsonSubTypes.Type(value = ModifyGameNumberAttributeEffect.class, name = "ModifyPlayerNumberAttributeEffect"),
        @JsonSubTypes.Type(value = Spanish41DrawCardEffect.class, name = "Spanish41DrawCardEffect"),
        @JsonSubTypes.Type(value = Spanish41BaseEffect.class, name = "Spanish41BaseEffect")
})
public interface CardEffect {
    Map<String, String> getRequiredInputs();
    void applyEffect(GameState state, PlayerActionRequest action, Card card);
}
