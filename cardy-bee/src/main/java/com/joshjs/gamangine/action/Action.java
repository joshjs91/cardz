package com.joshjs.gamangine.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;

import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayCardAction.class, name = "PlayCardAction"),
        @JsonSubTypes.Type(value = ChooseCardToDiscardAction.class, name = "ChooseCardToDiscardAction"),
        @JsonSubTypes.Type(value = EndTurnAction.class, name = "EndTurnAction"),
})
public interface Action {
    void execute(GameState state, PlayerActionRequest action);
    Map<String, String> getRequiredInputs();
    Boolean isRequired();
}
