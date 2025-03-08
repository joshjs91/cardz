package com.joshjs.gamangine.card.effects.spanish41;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.joshjs.gamangine.action.BaseAction;
import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.action.PlayCardAction;
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

@JsonTypeName("Spanish41DrawCardEffect")
@Data
public class Spanish41DrawCardEffect implements CardEffect {

    //TODO move these into the card type
    @NotBlank
    private String colour;

    @NotBlank
    private Integer cardsToDraw;

    @Override
    public Map<String, String> getRequiredInputs() {
        HashMap<String, String> requiredInputs = new HashMap<>();
        requiredInputs.put("colour", "String");
        return requiredInputs;
    }

    @Override
    public List<String> getFixedAttributes() {
        return new ArrayList<>(List.of("cardsToDraw"));
    }

    @Override
    public void applyEffect(GameState state, PlayerActionRequest action) {
        System.out.println("Applying effect 1");
        Optional<Card> lastPlayedCard = getLastPlayedCard(state);

        if (lastPlayedCard.isEmpty()) {
            applyNewDrawCard(state, action);
            return;
        }

        Optional<Spanish41DrawCardEffect> previousEffectOpt = getPreviousDrawCardEffect(lastPlayedCard.get());

        if (previousEffectOpt.isPresent()) {
            Spanish41DrawCardEffect previousEffect = previousEffectOpt.get();
            Optional<DrawCardAction> userDrawActionOpt = getUserDrawAction(state, action);

            if (userDrawActionOpt.isPresent()) {
                handleDrawCardInteraction(state, action, previousEffect, userDrawActionOpt.get());
                return;
            }
        }

        applyNewDrawCard(state, action);
    }

    /**
     * Extracts the previous Spanish41DrawCardEffect from a given card.
     */
    private Optional<Spanish41DrawCardEffect> getPreviousDrawCardEffect(Card card) {
        return card.getEffects().stream()
                .filter(Spanish41DrawCardEffect.class::isInstance)
                .map(Spanish41DrawCardEffect.class::cast)
                .findFirst();
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
     * Handles the interaction when a player tries to counter a previous draw card.
     */
    private void handleDrawCardInteraction(GameState state, PlayerActionRequest action,
                                           Spanish41DrawCardEffect previousEffect, DrawCardAction userDrawAction) {
        if (this.cardsToDraw >= previousEffect.cardsToDraw) {
            int newTotalDraw = userDrawAction.getCardsToDraw() + this.cardsToDraw;
            updateGameStateForNewDraw(state, action, newTotalDraw);
        } else {
            throw new InvalidInputException("The Spanish41 draw card played can't debuff previous draw+ card. Play another card or draw.");
        }
    }

    /**
     * Applies a new Draw X card to the game state.
     */
    private void applyNewDrawCard(GameState state, PlayerActionRequest action) {
        updateGameStateForNewDraw(state, action, this.cardsToDraw);
    }

    /**
     * Updates the game state by removing actions from the current player,
     * changing turns, and adding new draw actions.
     */
    private void updateGameStateForNewDraw(GameState state, PlayerActionRequest action, int cardsToDraw) {
        state.removePlayersActions(action.playerId);
        System.out.println("Changing turns beccuse ");
        state.changeTurns();
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(cardsToDraw);
        state.addActionsToPlayer(state.getCurrentPlayer(), List.of(new PlaySpanish41CardAction(), drawCardAction));
    }

}