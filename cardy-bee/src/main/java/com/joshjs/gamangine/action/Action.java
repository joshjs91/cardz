package com.joshjs.gamangine.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayCardAction.class, name = "PlayCardActionHandler"),
        @JsonSubTypes.Type(value = ChooseCardToDiscard.class, name = "ChooseCardToDiscardHandler"),
        @JsonSubTypes.Type(value = EndTurnAction.class, name = "EndTurnActionHandler"),
})
public interface Action {
    void execute(GameState state, PlayerActionRequest action);
}
