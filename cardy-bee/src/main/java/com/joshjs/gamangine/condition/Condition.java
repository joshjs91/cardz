package com.joshjs.gamangine.condition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.joshjs.gamangine.model.state.GameState;

import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AllPlayersHaveNoCardsInHandCondition.class, name = "AllPlayersHaveNoCardsInHandCondition"),
        @JsonSubTypes.Type(value = PlayerHasNoCardsInHandCondition.class, name = "PlayerHasNoCardsInHandCondition"),
        @JsonSubTypes.Type(value = GameNumberAttributeCondition.class, name = "GameNumberAttributeCondition"),
        @JsonSubTypes.Type(value = EmptyDeckCondition.class, name = "EmptyDeckCondition"),
})
public interface Condition {
    Map<String, String> getRequiredInputs();
    boolean evaluate(GameState state);
}