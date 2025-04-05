package com.joshjs.gamangine.card.effects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.joshjs.gamangine.validator.UserInputValidator.getNewIntValue;

@JsonTypeName("ModifyGameAttributeEffect")
@Data
public class ModifyGameNumberAttributeEffect implements CardEffect {

    //TODO these attributes this can be put into an abstract class?
    @JsonProperty("modificationValue")
    private Integer modificationValue;

    @JsonProperty("calculationType")
    @NotBlank
    private String calculationType;

    @JsonProperty("attribute")
    @NotBlank
    private String attribute;

    //TODO check these come back in the response of game state - needs to be visible...
    @Override
    public Map<String, String> getRequiredInputs() {
        HashMap<String, String> requiredInputs = new HashMap<>();
        return requiredInputs;
    }

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action, Card card) {
        //TODO need to deal with the game attribute not existing here or not being an Integer
        Integer currentValue = (Integer) state.getGameAttributes().get(attribute);
        Integer newValue = getNewIntValue(currentValue, calculationType, modificationValue);
        state.getGameAttributes().put(attribute,  + newValue);
    }


}
