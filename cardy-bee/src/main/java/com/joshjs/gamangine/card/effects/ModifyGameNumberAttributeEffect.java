package com.joshjs.gamangine.card.effects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static com.joshjs.gamangine.validator.UserInputValidator.getNewIntValue;

@JsonTypeName("ModifyGameAttributeEffect")
@Data
public class ModifyGameNumberAttributeEffect implements CardEffect {

    //TODO these attributes this can be put into an abstract class?
    @JsonProperty("modificationValue")
    private Integer modificationValue;

    @JsonProperty("calculationType")
    private String calculationType;

    @JsonProperty("attribute")
    private String attribute;

    @Override
    public Map<String, String> getRequiredInputs() {
        HashMap<String, String> requiredInputs = new HashMap<>();
        requiredInputs.put("attribute", "String");
        //TODO deal with some set of values somewhere?
        requiredInputs.put("calculationType", "SOME_SET_OF_VALUES");
        requiredInputs.put("modificationValue", "Integer");
        return requiredInputs;
    }

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action) {
        //TODO need to deal with the game attribute not existing here or not being an Integer
        Integer currentValue = (Integer) state.getGameAttributes().get(attribute);
        Integer newValue = getNewIntValue(currentValue, calculationType, modificationValue);
        state.getGameAttributes().put(attribute,  + newValue);
    }


}
