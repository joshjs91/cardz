package com.joshjs.gamangine.card.effects.spanish41;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.action.BaseAction;
import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.action.spanish41.PlaySpanish41CardAction;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.NumberAndColourCard;
import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.*;

import static com.joshjs.gamangine.card.effects.EffectHelpers.getLastPlayedCard;

@JsonTypeName("Spanish41DrawCardEffect")
@Data
public class Spanish41DrawCardEffect implements CardEffect {

    @NotBlank
    private Integer cardsToDraw;

    @Override
    public Map<String, String> getRequiredInputs() {
        return new HashMap<>();
    }

    //draw card can be played on any other card as long EXCEPT if the draw card action they currently have is less than their current draw card action
    @Override
    public void applyEffect(GameState state, PlayerActionRequest action, Card card) {
        int cardsToDraw = this.cardsToDraw;
        Optional<DrawCardAction> usersCurrentRequiredDrawCardActionOpt = getUserDrawAction(state, action);
        if (usersCurrentRequiredDrawCardActionOpt.isPresent()) {
            DrawCardAction drawCardAction = usersCurrentRequiredDrawCardActionOpt.get();
            if (this.cardsToDraw >= drawCardAction.getCardsToDraw()) {
                cardsToDraw = drawCardAction.getCardsToDraw() + this.cardsToDraw;
            } else {
                throw new InvalidInputException("The draw card played can't void current draw card actioned. Play another card or draw.");
            }
        }
        updateGameStateForNewDraw(state, action, cardsToDraw);
    }
    /**
     * Gets the player's active DrawCardAction that is REQUIRED i.e. forced. Only then it can be debuffed by playing a draw x card.
     */
    private Optional<DrawCardAction> getUserDrawAction(GameState state, PlayerActionRequest actionRequest) {
        return state.getPlayerAvailableActions()
                .getOrDefault(actionRequest.playerId, List.of())
                .stream()
                .filter(DrawCardAction.class::isInstance)
                .map(DrawCardAction.class::cast)
                //If its greater than 1 than they need to either draw a card or play a card to debuf the draw card requirement
                .filter(BaseAction::isRequired)
                .findFirst();
    }

    /**
     * Updates the game state by removing actions from the current player,
     * changing turns, and adding new draw actions.
     */
    private void updateGameStateForNewDraw(GameState state, PlayerActionRequest action, int cardsToDraw) {
        state.removePlayersActions(action.playerId);
        System.out.println("Changing turns");
        state.changeTurns();
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(cardsToDraw);
        state.addActionsToPlayer(state.getCurrentPlayer(), List.of(new PlaySpanish41CardAction(), drawCardAction));
    }

}