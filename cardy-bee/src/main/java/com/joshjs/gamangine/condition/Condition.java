package com.joshjs.gamangine.condition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.joshjs.gamangine.model.state.GameState;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CardsAllPlayedCondition.class, name = "CardsAllPlayedCondition"),
        @JsonSubTypes.Type(value = GameNumberAttributeCondition.class, name = "GameTokensCentralisedCondition"),
})
public interface Condition {
    boolean evaluate(GameState state);
}