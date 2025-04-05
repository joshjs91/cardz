package com.joshjs.gamangine.action.validator;

import com.joshjs.gamangine.action.validator.ActionValidator;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.NumberAndColourCard;
import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.card.effects.DiscardCardEffect;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41BaseEffect;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41DrawCardEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ActionValidatorTest {

    private GameState gameState;
    private String playerId = "player1";
    private NumberAndColourCard cardWithDrawEffect;
    private NumberAndColourCard cardWithBaseEffect;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        gameState.setPlayers(List.of(playerId));
        gameState.setPlayerHands(new HashMap<>());

        DiscardCardEffect drawEffect = new DiscardCardEffect();

        Spanish41BaseEffect baseEffect = new Spanish41BaseEffect();

        cardWithDrawEffect = new NumberAndColourCard("Draw 2", "red", 2, List.of(drawEffect));
        cardWithBaseEffect = new NumberAndColourCard("Draw 2", "red", 2, List.of(baseEffect));

        // Add draw-effect card to player's hand
        gameState.getPlayerHands().put(playerId, new ArrayList<>(List.of(cardWithDrawEffect)));
    }

    @Test
    void validatePlayCardActionAndGetCardFromHand_validCardAndInputs_passes() {
        PlayerActionRequest action = new PlayerActionRequest();
        action.setPlayerId(playerId);
        action.setInputs(Map.of("targetPlayer", "player2"));

        Card result = ActionValidator.validatePlayCardActionAndGetCardFromHand(gameState, cardWithDrawEffect, action);
        assertEquals(cardWithDrawEffect, result);
    }

    @Test
    void validatePlayCardActionAndGetCardFromHand_cardNotInHand_throws() {
        PlayerActionRequest action = new PlayerActionRequest();
        action.setPlayerId(playerId);
        action.setInputs(Map.of("targetPlayer", "player2"));

        NumberAndColourCard notInHand = new NumberAndColourCard("Draw 2", "red", 2, List.of());

        assertThrows(InvalidInputException.class, () ->
                ActionValidator.validatePlayCardActionAndGetCardFromHand(gameState, notInHand, action));
    }

    @Test
    void validatePlayCardActionAndGetCardFromHand_missingRequiredInput_throws() {
        PlayerActionRequest action = new PlayerActionRequest();
        action.setPlayerId(playerId);
        action.setInputs(Map.of()); // missing required input

        assertThrows(InvalidInputException.class, () ->
                ActionValidator.validatePlayCardActionAndGetCardFromHand(gameState, cardWithDrawEffect, action));
    }

    @Test
    void validateRequiredEffectInputs_inputPresent_passes() {
        PlayerActionRequest action = new PlayerActionRequest();
        action.setInputs(Map.of("targetPlayer", "player2"));

        assertDoesNotThrow(() -> ActionValidator.validateRequiredEffectInputs(cardWithDrawEffect, action));
    }

    @Test
    void validateRequiredEffectInputs_inputMissing_throws() {
        PlayerActionRequest action = new PlayerActionRequest();
        action.setInputs(new HashMap<>());

        assertThrows(InvalidInputException.class, () ->
                ActionValidator.validateRequiredEffectInputs(cardWithDrawEffect, action));
    }

    @Test
    void validatePlayCardActionAndGetCardFromHand_sameNameDifferentEffects_throws() {
        // Same name, attributes, but different effect (baseEffect instead of drawEffect)
        PlayerActionRequest action = new PlayerActionRequest();
        action.setPlayerId(playerId);
        action.setInputs(Map.of("targetPlayer", "player2"));

        assertThrows(InvalidInputException.class, () ->
                ActionValidator.validatePlayCardActionAndGetCardFromHand(gameState, cardWithBaseEffect, action));
    }

    @Test
    void validatePlayCardActionAndGetCardFromHand_sameNameAndEffectsDifferentOrder_shouldFail() {
        // Only if the equals() in your effect list or card depends on strict order
        Spanish41DrawCardEffect drawEffect = new Spanish41DrawCardEffect();
        drawEffect.setCardsToDraw(2);
//        drawEffect.setRequiredInputs(Map.of("targetPlayer", "String"));

        Spanish41BaseEffect baseEffect = new Spanish41BaseEffect();

        // Create card with reversed effect order
        NumberAndColourCard reversedEffectCard = new NumberAndColourCard("Draw 2", "red", 2, List.of(baseEffect, drawEffect));
        gameState.getPlayerHands().put(playerId, List.of(
                new NumberAndColourCard("Draw 2", "red", 2, List.of(drawEffect, baseEffect)) // original
        ));

        PlayerActionRequest action = new PlayerActionRequest();
        action.setPlayerId(playerId);
        action.setInputs(Map.of("targetPlayer", "player2"));

        // Depends on equals() behavior — this may fail or pass based on implementation
        assertThrows(InvalidInputException.class, () ->
                ActionValidator.validatePlayCardActionAndGetCardFromHand(gameState, reversedEffectCard, action));
    }
}
