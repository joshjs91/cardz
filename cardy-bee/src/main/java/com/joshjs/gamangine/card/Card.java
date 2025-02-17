package com.joshjs.gamangine.card;

import com.joshjs.gamangine.action.PlayerAction;
import com.joshjs.gamangine.model.GameState;

import java.util.List;
import java.util.Map;

public class Card {
    public String name;
    public List<CardEffect> effects;
    public Map<String, Class<?>> requiredInputs;

    public Card(String name, List<CardEffect> effects, Map<String, Class<?>> requiredInputs) {
        this.name = name;
        this.effects = effects;
        this.requiredInputs = requiredInputs;
    }

    public void applyEffects(GameState state, PlayerAction action) {
        for (String requiredInputKey : requiredInputs.keySet()) {
            Object actionInputValue = action.actionData.get(requiredInputKey);
            if (actionInputValue == null) {
                throw new RuntimeException("Missing player action input!: " + requiredInputKey);
            }
            if (!actionInputValue.getClass().equals(requiredInputs.get(requiredInputKey))) {
                throw new RuntimeException("invalid user input!: " + requiredInputKey);
            }
        }

        System.out.println("Player " + action.playerId + " played card: " + name);
        for (CardEffect effect : effects) {
            effect.applyEffect(state, action);
        }
    }

    public Map<String, Class<?>> getRequiredInputs() {
        return requiredInputs;
    }
}
