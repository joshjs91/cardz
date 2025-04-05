package com.joshjs.gamangine.card;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.card.effects.CardEffect;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("ColourCard")
public class ColourCard extends Card {
    @NotBlank
    private String colour;

    public ColourCard(String name, String colour, List<CardEffect> effects) {
        super(name, effects);
        this.colour = colour;
    }
}
