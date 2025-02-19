package com.joshjs.gamangine.action.handlers;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.card.DiscardCardEffect;
import com.joshjs.gamangine.card.ModifyAttributeEffect;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.model.PlayerActionRequest;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayCardActionHandler.class, name = "PlayCardActionHandler"),
        @JsonSubTypes.Type(value = ChooseCardToDiscardHandler.class, name = "ChooseCardToDiscardHandler"),
        @JsonSubTypes.Type(value = EndTurnActionHandler.class, name = "EndTurnActionHandler"),
})
public interface ActionHandler {
    void execute(GameState state, PlayerActionRequest action);
}
