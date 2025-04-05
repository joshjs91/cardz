package com.joshjs.gamangine.card.effects.spanish41;

import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.joshjs.gamangine.action.BaseAction;
import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.action.spanish41.PlaySpanish41CardAction;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.NumberAndColourCard;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41BaseEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Spanish41BaseEffectTest {

    private GameState gameState;
    private PlayerActionRequest actionRequest;
    private String playerId = "player1";
    private String nextPlayerId = "player2";

    @BeforeEach
    void setUp() {
        gameState = new GameState();

        gameState.setPlayers(List.of(playerId, nextPlayerId));
        gameState.setDiscardPile(new ArrayList<>());
        gameState.setPlayerAvailableActions(new HashMap<>());
        gameState.getPlayerAvailableActions().put(playerId, new ArrayList<>());
        gameState.getPlayerAvailableActions().put(nextPlayerId, new ArrayList<>());
        gameState.setCurrentPlayer(playerId);

        actionRequest = new PlayerActionRequest();
        actionRequest.setPlayerId(playerId);
    }

    @Test
    void applyEffect_noLastPlayedCard_shouldSucceed() {
        Spanish41BaseEffect effect = new Spanish41BaseEffect();
        NumberAndColourCard card = new NumberAndColourCard("1 red", "red", 1, List.of(effect));

        effect.applyEffect(gameState, actionRequest, card);

        List<Action> actions = gameState.getPlayerAvailableActions().get(nextPlayerId);
        assertNotNull(actions);
        assertEquals(2, actions.size());
        assertTrue(actions.stream().anyMatch(a -> a instanceof DrawCardAction));
        assertTrue(actions.stream().anyMatch(a -> a instanceof PlaySpanish41CardAction));
    }

    @Test
    void applyEffect_matchingNumber_shouldSucceed() {
        Spanish41BaseEffect effect = new Spanish41BaseEffect();
        NumberAndColourCard lastCard = new NumberAndColourCard("1 red", "red", 1, List.of());
        NumberAndColourCard card = new NumberAndColourCard("1 blue", "blue", 1, List.of(effect));
        gameState.getDiscardPile().add(lastCard);

        effect.applyEffect(gameState, actionRequest, card);

        List<Action> actions = gameState.getPlayerAvailableActions().get(nextPlayerId);
        assertEquals(2, actions.size());
    }

    @Test
    void applyEffect_matchingColour_shouldSucceed() {
        Spanish41BaseEffect effect = new Spanish41BaseEffect();
        NumberAndColourCard lastCard = new NumberAndColourCard("1 red", "red", 1, List.of());
        NumberAndColourCard card = new NumberAndColourCard("2 red", "red", 2, List.of(effect));
        gameState.getDiscardPile().add(lastCard);

        effect.applyEffect(gameState, actionRequest, card);

        List<Action> actions = gameState.getPlayerAvailableActions().get(nextPlayerId);
        assertEquals(2, actions.size());
    }

    @Test
    void applyEffect_noMatch_shouldThrow() {
        Spanish41BaseEffect effect = new Spanish41BaseEffect();
        NumberAndColourCard lastCard = new NumberAndColourCard("1 red", "red", 1, List.of());
        NumberAndColourCard card = new NumberAndColourCard("2 blue", "blue", 2, List.of(effect));
        gameState.getDiscardPile().add(lastCard);

        assertThrows(InvalidInputException.class, () -> {
            effect.applyEffect(gameState, actionRequest, card);
        });
    }

    @Test
    void applyEffect_lastCardHasNoNumberOrColour_shouldThrow() {
        Spanish41BaseEffect effect = new Spanish41BaseEffect();

        // Anonymous class without getNumber/getColour
        Card lastCard = new Card("Mystery", List.of()) {};
        NumberAndColourCard card = new NumberAndColourCard("2 blue", "blue", 2, List.of(effect));

        gameState.getDiscardPile().add(lastCard);

        assertThrows(InvalidInputException.class, () -> {
            effect.applyEffect(gameState, actionRequest, card);
        });
    }
}

