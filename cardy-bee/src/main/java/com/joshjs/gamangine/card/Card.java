package com.joshjs.gamangine.card;

import com.joshjs.gamangine.model.PlayerActionRequest;
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
    public String name;
    public List<CardEffect> effects;
    public Map<String, Class<?>> requiredInputs;

    public void applyEffects(GameState state, PlayerActionRequest action) {
        //TODO this should be moved inside the effects

        verifyUserInput(requiredInputs, action);
        System.out.println("Player " + action.playerId + " played card: " + name);
        for (CardEffect effect : effects) {
            effect.applyEffect(state, action);
        }
    }
}
