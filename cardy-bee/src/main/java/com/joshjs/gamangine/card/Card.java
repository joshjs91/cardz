package com.joshjs.gamangine.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumberAndColourCard.class, name = "NumberAndColourCard"),
        @JsonSubTypes.Type(value = NumberCard.class, name = "NumberCard"),
        @JsonSubTypes.Type(value = ColourCard.class, name = "ColourCard")
})
public class Card {

    @NotBlank
    private String name;

    @JsonProperty("effects")
    private List<CardEffect> effects = new ArrayList<>();

    /**
     * Applies all effects of this card.
     */
    public void applyEffects(GameState state, PlayerActionRequest action) {
        System.out.println("Player " + action.playerId + " played card: " + name);
        for (CardEffect effect : effects) {
            effect.applyEffect(state, action, this);
        }
    }
}
