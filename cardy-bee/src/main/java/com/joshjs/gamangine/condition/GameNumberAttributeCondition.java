package com.joshjs.gamangine.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.state.GameState;

//TODO this can be the same pattern for card effects??????? i.e. it deserlizes with the value of the variable and can be saved as state....
@JsonTypeName("GameNumberAttributeCondition")
public class GameNumberAttributeCondition implements Condition {

    @JsonProperty("conditionValue")
    private Integer conditionValue;

    @JsonProperty("calculationType")
    private String calculationType;

    //TODO can add another value to represent what type attribute is???
    @JsonProperty("attribute")
    private String attribute;

    @Override
    public boolean evaluate(GameState state) {
        int gameAttributeValue = (Integer) state.getGameAttributes().get(attribute);
        return switch (calculationType.toLowerCase()) {
            case "greaterthan" -> gameAttributeValue > conditionValue;
            case "lessthan" -> gameAttributeValue < conditionValue;
            case "equals" -> gameAttributeValue == conditionValue;
            default -> throw new IllegalArgumentException("Invalid calculation type: " + calculationType);
        };
    }
}
