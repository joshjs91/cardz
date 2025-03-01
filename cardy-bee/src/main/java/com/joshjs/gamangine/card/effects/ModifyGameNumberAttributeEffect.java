package com.joshjs.gamangine.card.effects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.joshjs.gamangine.validator.UserInputValidator.getNewIntValue;

//TODO need to figure how to add attributes to a card....i.e. field to modify or colour or number...?
// maybe a hack could be to check non null attributes equal on the card as well as in played card...? or final attributes?
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

    //TODO check these come back in the response of game state - needs to be visible...
    @Override
    public Map<String, String> getRequiredInputs() {
        HashMap<String, String> requiredInputs = new HashMap<>();
        requiredInputs.put("modificationValue", "Integer");
        return requiredInputs;
    }

    //TODO check these come back in the response of game state - needs to be visible...
    @Override
    public List<String> getFixedAttributes() {
        return List.of("calculationType", "attribute");
    }

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action) {
        //TODO need to deal with the game attribute not existing here or not being an Integer
        Integer currentValue = (Integer) state.getGameAttributes().get(attribute);
        Integer newValue = getNewIntValue(currentValue, calculationType, modificationValue);
        state.getGameAttributes().put(attribute,  + newValue);
    }


}
