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

@JsonTypeName("ModifyPlayerNumberAttributeEffect")
@Data
public class ModifyPlayerNumberAttributeEffect implements CardEffect {

    @JsonProperty("modificationValue")
    private Integer modificationValue;

    @JsonProperty("calculationType")
    @NotBlank
    private String calculationType;

    @JsonProperty("attribute")
    @NotBlank
    private String attribute;

    @JsonProperty("targetPlayer")
    private String targetPlayer;

    @Override
    public Map<String, String> getRequiredInputs() {
        HashMap<String, String> requiredInputs = new HashMap<>();
        requiredInputs.put("targetPlayer", "String");
        return requiredInputs;
    }

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action, Card card) {
        Map<String, Object> targetPlayerAttributes = state.getPlayerAttributes().get(targetPlayer);
        if (targetPlayer == null || targetPlayerAttributes == null) {
            throw new IllegalArgumentException("Invalid target player: " + targetPlayer);
        }
        Integer currentValue = (Integer) targetPlayerAttributes.get(attribute);
        if (currentValue == null) {
            throw new IllegalArgumentException("Invalid player attribute to modify: " + targetPlayer);
        }
        Integer newValue = getNewIntValue(currentValue, calculationType, modificationValue);
        state.getGameAttributes().put(attribute, newValue);
    }
}
