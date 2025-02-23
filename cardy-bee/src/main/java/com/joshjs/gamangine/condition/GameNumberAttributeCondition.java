package com.joshjs.gamangine.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.state.GameState;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@JsonTypeName("GameNumberAttributeCondition")
@Data
public class GameNumberAttributeCondition implements Condition {

    @JsonProperty("modificationValue")
    private Integer modificationValue;

    @JsonProperty("calculationType")
    private String calculationType;

    @JsonProperty("attribute")
    private String attribute;

    @Override
    public Map<String, String> getRequiredInputs() {
        HashMap<String, String> requiredInputs = new HashMap<>();
        requiredInputs.put("attributeValue", "Integer");
        //TODO deal with some set of values somewhere?
        requiredInputs.put("calculationType", "SOME_SET_OF_VALUES");
        requiredInputs.put("attribute", "String");
        return requiredInputs;
    }

    @Override
    public boolean evaluate(GameState state) {

        //TODO needs to do error handling of null calculationType etc.
        Integer gameAttributeValue = (Integer) state.getGameAttributes().get(attribute);

        if (gameAttributeValue == null) {
            throw new IllegalArgumentException("Game does not have attribute: " + attribute);
        }

        return switch (calculationType.toLowerCase()) {
            case "greaterthan" -> gameAttributeValue > modificationValue;
            case "lessthan" -> gameAttributeValue < modificationValue;
            case "equals" -> gameAttributeValue.equals(modificationValue);
            default -> throw new IllegalArgumentException("Invalid calculation type: " + calculationType);
        };
    }
}
