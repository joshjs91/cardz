package com.joshjs.gamangine.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;

@JsonTypeName("ModifyGameAttributeEffect")
public class ModifyGameNumberAttributeEffect implements CardEffect {

    //TODO these attributes this can be put into an abstract class?
    @JsonProperty("attributeValue")
    private Integer attributeValue;

    @JsonProperty("calculationType")
    private String calculationType;

    @JsonProperty("attribute")
    private String attribute;

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action) {
        //TODO need to deal with the game attribute not existing here or not being an Integer
        Integer currentValue = (Integer) state.getGameAttributes().get(attribute);
        Integer newValue = switch (calculationType.toLowerCase()) {
            case "multiplyby" -> currentValue * attributeValue;
            case "minus" -> currentValue - attributeValue;
            case "add" -> currentValue + attributeValue;
            default -> throw new IllegalArgumentException("Invalid calculation type: " + calculationType);
        };
        state.getGameAttributes().put(attribute,  + newValue);
    }
}
