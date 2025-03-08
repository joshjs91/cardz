package com.joshjs.gamangine.action;

import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.effects.CardEffect;
import com.joshjs.gamangine.card.effects.ModifyGameNumberAttributeEffect;
import com.joshjs.gamangine.card.effects.UselessEffect;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PlayCardActionTest {

    private GameState gameState;
    private PlayCardAction playCardAction;
    private PlayerActionRequest actionRequest;
    private String playerId = "player1";

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        HashMap<String, Object> gameAttributes = new HashMap<>();
        gameAttributes.put("tokens", 0);
        gameState.setGameAttributes(gameAttributes);
        gameState.setDiscardPile(new ArrayList<>());
        playCardAction = new PlayCardAction();
        actionRequest = new PlayerActionRequest();
        actionRequest.setPlayerId(playerId);

        // Set up player hand
        Map<String, List<Card>> playerHands = new HashMap<>();
        gameState.setPlayerHands(playerHands);
        List<Card> playersHand = new ArrayList<>();
        playersHand.add(new Card("CardyMcdo", List.of(new UselessEffect())));
        playerHands.put(playerId, playersHand);
    }

    @Test
    void testPlayValidCard_Success() {
        ModifyGameNumberAttributeEffect effectOfCardInHand = new ModifyGameNumberAttributeEffect();
        effectOfCardInHand.setCalculationType("add");
        effectOfCardInHand.setAttribute("tokens");
        Card cardInHand = new Card("Attack", List.of(effectOfCardInHand));
        gameState.getPlayerHands().get(playerId).add(cardInHand);
        assertTrue(gameState.getPlayerHands().get(playerId).contains(cardInHand), "Card should be in hand");

        ModifyGameNumberAttributeEffect effectOfCardPlayed = new ModifyGameNumberAttributeEffect();
        effectOfCardPlayed.setCalculationType("add");
        effectOfCardPlayed.setAttribute("tokens");
        effectOfCardPlayed.setModificationValue(10);
        playCardAction.setPlayedCard(new Card("Attack", List.of(effectOfCardPlayed)));

        playCardAction.execute(gameState, actionRequest);

        assertFalse(gameState.getPlayerHands().get(playerId).contains(cardInHand), "Card should be removed from hand");
        assertTrue(gameState.getDiscardPile().contains(cardInHand), "Card should be added to discard pile");
    }

    @Test
    void testPlayCard_NotInHand_ThrowsException() {
        ModifyGameNumberAttributeEffect effectOfCardPlayed = new ModifyGameNumberAttributeEffect();
        effectOfCardPlayed.setCalculationType("add");
        effectOfCardPlayed.setAttribute("tokens");
        effectOfCardPlayed.setModificationValue(10);
        playCardAction.setPlayedCard(new Card("Attack", List.of(effectOfCardPlayed)));

        Exception thrownException = assertThrows(InvalidInputException.class, () -> playCardAction.execute(gameState, actionRequest));
        assertEquals("Player does not have a valid card named: Attack", thrownException.getMessage());

    }

    @Test
    void testPlayCard_NothingInHand_ThrowsException() {
        gameState.getPlayerHands().get(playerId).clear();
        ModifyGameNumberAttributeEffect effectOfCardPlayed = new ModifyGameNumberAttributeEffect();
        effectOfCardPlayed.setCalculationType("add");
        effectOfCardPlayed.setAttribute("tokens");
        effectOfCardPlayed.setModificationValue(10);
        playCardAction.setPlayedCard(new Card("Attack", List.of(effectOfCardPlayed)));

        Exception thrownException = assertThrows(InvalidInputException.class, () -> playCardAction.execute(gameState, actionRequest));
        assertEquals("Player does not have a valid card named: Attack", thrownException.getMessage());
    }

    @Test
    void testPlayCard_EffectsMismatch_ThrowsException() {
        ModifyGameNumberAttributeEffect effectOfCardPlayed = new ModifyGameNumberAttributeEffect();
        effectOfCardPlayed.setCalculationType("add");
        effectOfCardPlayed.setAttribute("tokens");
        effectOfCardPlayed.setModificationValue(10);
        Card cardInHand = new Card("Attack", List.of(effectOfCardPlayed));
        gameState.getPlayerHands().get(playerId).add(cardInHand);

        // Played card has a different effect class
        playCardAction.setPlayedCard(new Card("Attack", List.of(mock(CardEffect.class))));

        Exception thrownException = assertThrows(InvalidInputException.class, () -> playCardAction.execute(gameState, actionRequest));
        assertEquals("Player has card 'Attack' but with different effect types.", thrownException.getMessage());

    }

    @Test
    void testPlayCard_EffectAttributesMismatch_ThrowsException() {
        ModifyGameNumberAttributeEffect effectOfCardInHand = new ModifyGameNumberAttributeEffect();
        effectOfCardInHand.setCalculationType("minus");
        effectOfCardInHand.setAttribute("tokens");
        Card cardInHand = new Card("Attack", List.of(effectOfCardInHand));
        gameState.getPlayerHands().get(playerId).add(cardInHand);

        // Played card has the same effect but with different attribute values
        ModifyGameNumberAttributeEffect effectOfCardPlayed = new ModifyGameNumberAttributeEffect();
        effectOfCardPlayed.setCalculationType("add");
        effectOfCardPlayed.setAttribute("tokens");
        effectOfCardPlayed.setModificationValue(5);
        playCardAction.setPlayedCard(new Card("Attack", List.of(effectOfCardPlayed)));

        Exception thrownException = assertThrows(InvalidInputException.class, () -> playCardAction.execute(gameState, actionRequest));
        assertEquals("Effect attributes were tampered with for card: Attack", thrownException.getMessage());
    }

    @Test
    void testPlayCard_CardRemovedAndAddedToDiscardPile() {
        ModifyGameNumberAttributeEffect effectOfCardInHand = new ModifyGameNumberAttributeEffect();
        effectOfCardInHand.setCalculationType("minus");
        effectOfCardInHand.setAttribute("tokens");
        Card cardInHand = new Card("Attack", List.of(effectOfCardInHand));
        gameState.getPlayerHands().get(playerId).add(cardInHand);
        assertTrue(gameState.getPlayerHands().get(playerId).contains(cardInHand), "Card should be in hand");
        ModifyGameNumberAttributeEffect effectOfCardPlayed = new ModifyGameNumberAttributeEffect();
        effectOfCardPlayed.setCalculationType("minus");
        effectOfCardPlayed.setAttribute("tokens");
        effectOfCardPlayed.setModificationValue(5);
        playCardAction.setPlayedCard(new Card("Attack", List.of(effectOfCardPlayed)));

        playCardAction.execute(gameState, actionRequest);

        assertFalse(gameState.getPlayerHands().get(playerId).contains(cardInHand), "Card should be removed from hand");
        assertTrue(gameState.getDiscardPile().contains(cardInHand), "Card should be added to discard pile");
    }
}