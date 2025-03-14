package com.joshjs.gamangine.card;

import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String name;
    private List<CardEffect> effects = new ArrayList<>();

    public void applyEffects(GameState state, PlayerActionRequest action) {
        System.out.println("Player " + action.playerId + " played card: " + name);
        for (CardEffect effect : effects) {
            effect.applyEffect(state, action);
        }
    }
}
