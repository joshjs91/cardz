package com.joshjs.gamangine.card.effects.spanish41;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.action.PlayCardAction;
import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.action.spanish41.PlaySpanish41CardAction;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.*;

import static com.joshjs.gamangine.card.effects.EffectHelpers.getLastPlayedCard;

@JsonTypeName("Spanish41BaseEffect")
@Data
public class Spanish41BaseEffect implements CardEffect {

    @NotBlank
    private String colour;

    @NotBlank
    private Integer number;

    @Override
    public Map<String, String> getRequiredInputs() {
        return new HashMap<>();
    }

    @Override
    public List<String> getFixedAttributes() {
        return new ArrayList<>(List.of("colour", "number"));
    }

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action) {
        Optional<Card> lastPlayedCard = getLastPlayedCard(state);

        if (lastPlayedCard.isPresent()) {
            // Get the last played card effect to check its color and number

            Optional<Spanish41BaseEffect> firstSp41EffectOpt = lastPlayedCard.get()
                    .getEffects()
                    .stream()
                    .filter(effect -> effect instanceof Spanish41BaseEffect)
                    .map(effect -> (Spanish41BaseEffect) effect) // Safe casting
                    .findFirst();

            if (firstSp41EffectOpt.isPresent()) {
                Spanish41BaseEffect firstSp41Effect = firstSp41EffectOpt.get();
                if (!this.number.equals(firstSp41Effect.number) && !this.colour.equals(firstSp41Effect.colour)) {
                    throw new InvalidInputException("You must play a card that matches either the color or number!");
                }
            }
            //TODO theres probably an IFF scneario here when other type of cards are playing (possible bug here)

        }
        changeTurnAndSetActions(state, action.playerId);
    }

    private void changeTurnAndSetActions(GameState state, String player) {
        state.removePlayersActions(player);
        state.changeTurns();
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(1);
        state.addActionsToPlayer(state.getCurrentPlayer(), List.of(new PlaySpanish41CardAction(), drawCardAction));
    }
}
