package com.joshjs.gamangine.card;

import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    //TODO make private
    private String name;
    private List<CardEffect> effects;

    public void applyEffects(GameState state, PlayerActionRequest action) {
        System.out.println("Player " + action.playerId + " played card: " + name);
        for (CardEffect effect : effects) {
            effect.applyEffect(state, action);
        }
    }


    @Override
    public boolean equals(Object o) {
        // If comparing with itself, return true
        if (this == o) return true;

        // If the other object is not of the same class, return false
        if (o == null || getClass() != o.getClass()) return false;

        // Cast to Card to compare internal properties
        Card card = (Card) o;

        // Check if names are equal
        boolean areNamesEqual = Objects.equals(name, card.name);

        // Use the helper method to check if the CardEffect class types match
        boolean areEffectsClassTypesEqual = areEffectsClassTypesEqual(card);

        // Return true if both name and effects match
        return areNamesEqual && areEffectsClassTypesEqual;
    }

    // Helper method to compare the class types of effects
    private boolean areEffectsClassTypesEqual(Card card) {
        if (effects.size() != card.getEffects().size()) {
            return false; // If the number of effects is different, they cannot be equal
        }

        for (int i = 0; i < effects.size(); i++) {
            // Compare class types of the CardEffect objects
            if (!effects.get(i).getClass().equals(card.getEffects().get(i).getClass())) {
                return false;
            }
        }

        return true; // If all class types match
    }

    @Override
    public int hashCode() {
        // Hash based on name and effects class types
        return Objects.hash(name, effects);
    }
}
