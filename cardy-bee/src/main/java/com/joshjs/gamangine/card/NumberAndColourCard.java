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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonTypeName("NumberAndColourCard")
public class NumberAndColourCard extends Card {
    @NotBlank
    private String colour;
    @NotBlank
    private Integer number;

    public NumberAndColourCard(String name, String colour, Integer number, List<CardEffect> effects) {
        super(name, effects);
        this.colour = colour;
        this.number = number;
    }
}
