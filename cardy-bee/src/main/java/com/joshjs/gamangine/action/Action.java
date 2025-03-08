package com.joshjs.gamangine.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.action.spanish41.PlaySpanish41CardAction;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayCardAction.class, name = "PlayCardAction"),
        @JsonSubTypes.Type(value = DiscardCardAction.class, name = "DiscardCardAction"),
        @JsonSubTypes.Type(value = DrawCardAction.class, name = "DrawCardAction"),
        @JsonSubTypes.Type(value = PlaySpanish41CardAction.class, name = "PlaySpanish41CardAction"),
        @JsonSubTypes.Type(value = EndTurnAction.class, name = "EndTurnAction"),
})
public interface Action {
    void execute(GameState state, PlayerActionRequest action);
    Map<String, String> getRequiredInputs();
    List<String> getFixedAttributes();
    Boolean isRequired();
}
