package com.joshjs.gamangine.card;

import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static com.joshjs.gamangine.validator.UserInputValidator.verifyUserInput;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    //TODO make private
    private String name;
    private List<CardEffect> effects;
    //TODO these should be moved into the effects as attributes on the effect as
    private Map<String, Class<?>> requiredInputs;

    public void applyEffects(GameState state, PlayerActionRequest action) {
        //TODO this should be moved inside the effects
        verifyUserInput(requiredInputs, action);
        System.out.println("Player " + action.playerId + " played card: " + name);
        for (CardEffect effect : effects) {
            effect.applyEffect(state, action);
        }
    }
}
