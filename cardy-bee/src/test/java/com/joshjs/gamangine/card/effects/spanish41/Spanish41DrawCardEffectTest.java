package com.joshjs.gamangine.card.effects.spanish41;

import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41DrawCardEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Spanish41DrawCardEffectTest {

    private GameState gameState;
    private PlayerActionRequest actionRequest;
    private String playerId = "player1";
    private String nextPlayerId = "player2";

    @BeforeEach
    void setUp() {
        gameState = new GameState();

        gameState.setPlayers(new ArrayList<>(List.of(playerId, nextPlayerId)));
        gameState.setDiscardPile(new ArrayList<>());
        gameState.setPlayerAvailableActions(new HashMap<>());
        gameState.getPlayerAvailableActions().put(playerId, new ArrayList<>());

        actionRequest = new PlayerActionRequest();
        actionRequest.setPlayerId(playerId);

        gameState.setCurrentPlayer(playerId);
    }

    @Test
    void applyEffect_noCurrentDrawAction_shouldApplyDrawEffect() {
        Spanish41DrawCardEffect effect = new Spanish41DrawCardEffect();
        effect.setCardsToDraw(2);

        effect.applyEffect(gameState, actionRequest, null);

        List<?> newActions = gameState.getPlayerAvailableActions().get(nextPlayerId);
        assertNotNull(newActions);
        assertTrue(newActions.stream().anyMatch(a -> a instanceof DrawCardAction));

        DrawCardAction drawAction = (DrawCardAction) newActions.stream()
                .filter(a -> a instanceof DrawCardAction)
                .findFirst()
                .orElseThrow();

        assertEquals(2, drawAction.getCardsToDraw());
    }

    @Test
    void applyEffect_withExistingDrawAction_shouldStackEffect() {
        DrawCardAction existingDraw = new DrawCardAction();
        existingDraw.setCardsToDraw(2);
        existingDraw.setRequired(true); // must be required for stacking to work

        gameState.getPlayerAvailableActions().get(playerId).add(existingDraw);

        Spanish41DrawCardEffect effect = new Spanish41DrawCardEffect();
        effect.setCardsToDraw(3); // Should stack to 2 + 3 = 5

        effect.applyEffect(gameState, actionRequest, null);

        List<?> newActions = gameState.getPlayerAvailableActions().get(nextPlayerId);
        DrawCardAction drawAction = (DrawCardAction) newActions.stream()
                .filter(a -> a instanceof DrawCardAction)
                .findFirst()
                .orElseThrow();

        assertEquals(5, drawAction.getCardsToDraw());
    }

    @Test
    void applyEffect_withWeakerDrawCard_shouldThrow() {
        DrawCardAction existingDraw = new DrawCardAction();
        existingDraw.setCardsToDraw(5);
        existingDraw.setRequired(true);

        gameState.getPlayerAvailableActions().get(playerId).add(existingDraw);

        Spanish41DrawCardEffect effect = new Spanish41DrawCardEffect();
        effect.setCardsToDraw(2); // Too weak to cancel or stack

        assertThrows(InvalidInputException.class, () -> {
            effect.applyEffect(gameState, actionRequest, null);
        });
    }
}
