package com.joshjs.gamangine.card.effects.spanish41;

import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.effects.UselessEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Spanish41DrawCardEffectTest {

    private GameState gameState;
    private PlayerActionRequest actionRequest;
    private String playerId = "player1";

    @BeforeEach
    void setUp() {
        gameState = new GameState();

        gameState.setPlayers(new ArrayList<>(List.of(playerId, "player2")));
        gameState.setDiscardPile(new ArrayList<>()); // Ensure discard pile is empty at the start
        gameState.setPlayerAvailableActions(new HashMap<>());
        gameState.getPlayerAvailableActions().put(playerId, new ArrayList<>()); // Empty action list for player

        actionRequest = new PlayerActionRequest();
        actionRequest.setPlayerId(playerId);
    }

    /**
     * Test: When a new draw card is played and no previous draw card exists
     * Expected: The game should assign the next player a draw action with the correct number of cards.
     */
    @Test
    void testApplyEffect_NewDrawCard_AppliesSuccessfully() {
        Spanish41DrawCardEffect drawCardEffect = new Spanish41DrawCardEffect();
        drawCardEffect.setColour("Red");
        drawCardEffect.setCardsToDraw(2);
        drawCardEffect.applyEffect(gameState, actionRequest);

        List<?> nextActions = gameState.getPlayerAvailableActions().get(gameState.getCurrentPlayer());

        assertTrue(nextActions.stream().anyMatch(action -> action instanceof DrawCardAction), "A new DrawCardAction should be assigned.");
        assertEquals(2, ((DrawCardAction) nextActions.stream().filter(action -> action instanceof DrawCardAction).findFirst().get()).getCardsToDraw());
    }

    /**
     * Test: When a player plays a draw card that has a higher draw count than the previous draw card
     * Expected: The new draw count should be added to the previous, and passed to the next player.
     */
    @Test
    void testApplyEffect_CounterPreviousDrawCard_Success() {
        Spanish41DrawCardEffect previousEffect = new Spanish41DrawCardEffect();
        previousEffect.setCardsToDraw(2);
        previousEffect.setColour("Blue");
        Spanish41DrawCardEffect newEffect = new Spanish41DrawCardEffect();
        newEffect.setColour("Red");
        newEffect.setCardsToDraw(4);

        gameState.getDiscardPile().add(new Card("Draw Two", List.of(previousEffect))); // Previous draw card exists
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(2);
        drawCardAction.setRequired(true);
        gameState.getPlayerAvailableActions().get(playerId).add(drawCardAction); // Player has pending draw action

        newEffect.applyEffect(gameState, actionRequest);

        List<?> nextActions = gameState.getPlayerAvailableActions().get(gameState.getCurrentPlayer());

        assertTrue(nextActions.stream().anyMatch(action -> action instanceof DrawCardAction), "New DrawCardAction should be assigned.");
        assertEquals(6, ((DrawCardAction) nextActions.stream().filter(action -> action instanceof DrawCardAction).findFirst().get()).getCardsToDraw());
    }

    /**
     * Test: When a player tries to counter a previous draw card with a lower-value draw card
     * Expected: The action should be rejected, throwing an InvalidInputException.
     */
    @Test
    void testApplyEffect_InvalidCounterDrawCard_ThrowsException() {
        Spanish41DrawCardEffect previousEffect = new Spanish41DrawCardEffect();
        previousEffect.setCardsToDraw(4);
        previousEffect.setColour("Blue");
        Spanish41DrawCardEffect weakerEffect = new Spanish41DrawCardEffect(); // Lower draw count
        weakerEffect.setColour("Red");
        weakerEffect.setCardsToDraw(2);

        gameState.getDiscardPile().add(new Card("Draw Four", List.of(previousEffect)));
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(4);
        drawCardAction.setRequired(true);
        gameState.getPlayerAvailableActions().get(playerId).add(drawCardAction); // Previous draw action exists

        InvalidInputException thrownException = assertThrows(InvalidInputException.class, () -> weakerEffect.applyEffect(gameState, actionRequest));
        assertEquals("The Spanish41 draw card played can't debuff previous draw+ card. Play another card or draw.", thrownException.getMessage());
    }

    /**
     * Test: When a draw card is played on a normal (non-draw) card
     * Expected: The game should treat it as a new draw card and assign the next player a draw action.
     */
    @Test
    void testApplyEffect_DrawCardAfterNormalCard_AppliesSuccessfully() {
        Card normalCard = new Card("Number 5", List.of()); // Last played card was not a draw card
        gameState.getDiscardPile().add(normalCard);

        Spanish41DrawCardEffect newDrawCardEffect = new Spanish41DrawCardEffect();
        newDrawCardEffect.setCardsToDraw(3);
        newDrawCardEffect.setColour("Green");
        newDrawCardEffect.applyEffect(gameState, actionRequest);

        List<?> nextActions = gameState.getPlayerAvailableActions().get(gameState.getCurrentPlayer());

        assertTrue(nextActions.stream().anyMatch(action -> action instanceof DrawCardAction), "New DrawCardAction should be assigned.");
        assertEquals(3, ((DrawCardAction) nextActions.stream().filter(action -> action instanceof DrawCardAction).findFirst().get()).getCardsToDraw());
    }


    //TODO this is an integration test because it should never happen that a general Playcard action can get this far. Do this next with snap cards
    /**
     * Test: When a player is forced to draw and they play a non-draw card
     * Expected: The game should reject the action, forcing them to draw.
     */
    @Test
    void testApplyEffect_PlayerTriesToAvoidDraw_ThrowsException() {
        Spanish41DrawCardEffect previousEffect = new Spanish41DrawCardEffect();
        previousEffect.setColour("Blue");
        previousEffect.setCardsToDraw(2);

        gameState.getDiscardPile().add(new Card("Draw Two", List.of(previousEffect)));
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(2);
        drawCardAction.setRequired(true);
        gameState.getPlayerAvailableActions().get(playerId).add(drawCardAction); // Player needs to draw

        Card nonDrawCard = new Card("Some card", List.of(new UselessEffect())); // Invalid counterplay
        InvalidInputException thrownException = assertThrows(InvalidInputException.class, () -> nonDrawCard.getEffects().get(0).applyEffect(gameState, actionRequest));
        assertEquals("The Spanish41 draw card played can't debuff previous draw+ card. Play another card or draw.", thrownException.getMessage());
    }

    /**
     * Test: When a draw card is played correctly after another draw card of the same value
     * Expected: The previous draw count should be stacked onto the new draw count and assigned to the next player.
     */
    @Test
    void testApplyEffect_StackSameValueDrawCards_Success() {
        Spanish41DrawCardEffect previousEffect = new Spanish41DrawCardEffect();
        previousEffect.setCardsToDraw(2);
        previousEffect.setColour("Read");
        Spanish41DrawCardEffect sameValueEffect = new Spanish41DrawCardEffect();
        sameValueEffect.setColour("Blue");
        sameValueEffect.setCardsToDraw(2);

        gameState.getDiscardPile().add(new Card("Draw Two", List.of(previousEffect))); // Previous draw card exists
        DrawCardAction drawCardAction = new DrawCardAction();
        drawCardAction.setCardsToDraw(2);
        drawCardAction.setRequired(true);
        gameState.getPlayerAvailableActions().get(playerId).add(drawCardAction); // Player has pending draw action

        sameValueEffect.applyEffect(gameState, actionRequest);

        List<?> nextActions = gameState.getPlayerAvailableActions().get(gameState.getCurrentPlayer());

        assertTrue(nextActions.stream().anyMatch(action -> action instanceof DrawCardAction), "New DrawCardAction should be assigned.");
        assertEquals(4, ((DrawCardAction) nextActions.stream().filter(action -> action instanceof DrawCardAction).findFirst().get()).getCardsToDraw());
    }
}
