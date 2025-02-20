package com.joshjs.gamangine.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.state.GameState;

//TODO this can be the same pattern for card effects??????? i.e. it deserlizes with the value of the variable and can be saved as state....
@JsonTypeName("GameNumberAttributeCondition")
public class GameNumberAttributeCondition implements Condition {

    @JsonProperty("attributeValue")
    private Integer attributeValue;

    @JsonProperty("calculationType")
    private String calculationType;

    @JsonProperty("attribute")
    private String attribute;

    @Override
    public boolean evaluate(GameState state) {

        //TODO needs to do error handling of null calculationType etc.
        Integer gameAttributeValue = (Integer) state.getGameAttributes().get(attribute);

        if (gameAttributeValue == null) {
            throw new IllegalArgumentException("Game does not have attribute: " + attribute);
        }

        return switch (calculationType.toLowerCase()) {
            case "greaterthan" -> gameAttributeValue > attributeValue;
            case "lessthan" -> gameAttributeValue < attributeValue;
            case "equals" -> gameAttributeValue == attributeValue;
            default -> throw new IllegalArgumentException("Invalid calculation type: " + calculationType);
        };
    }
}
