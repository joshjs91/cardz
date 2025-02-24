package com.joshjs.gamangine.service;

import com.joshjs.gamangine.action.*;
import com.joshjs.gamangine.action.model.PendingAction;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class GameServiceTest {
    private GameService gameService;
    private GameState mockState;
    private PlayerActionRequest mockActionRequest;

    @BeforeEach
    void setUp() {
        gameService = new GameService();

        mockState = new GameState();
        mockState.setGameId("test-game");
        mockState.setPlayers(Arrays.asList("player1", "player2"));
        mockState.setCurrentPlayer("player1");
        mockState.setPlayerAvailableActions(new HashMap<>());
        mockState.setDiscardPile(new ArrayList<>());
        Map<String, List<Card>> playerHands = new HashMap<>();
        for (String player : mockState.getPlayers()) {
            List<Card> hand = new ArrayList<>();
            hand.add(new Card("No effects", new ArrayList<>()));
            playerHands.put(player, hand);
        }
        mockState.setPlayerHands(playerHands);
        mockActionRequest = new PlayerActionRequest();
        mockActionRequest.gameId = "test-game";
        mockActionRequest.playerId = "player1";
        mockActionRequest.action = new PlayCardAction();

        List<Action> actions = new ArrayList<>();
        actions.add(new PlayCardAction());
        BaseAction action = new EndTurnAction();
        action.setIsRequired(true);
        actions.add(action);
        mockState.getPlayerAvailableActions().put("player1", actions);

        gameService.games.put("test-game", mockState);
    }

    @Test
    void testExecuteActionAllowed() {
        PlayCardAction action = new PlayCardAction();
        action.setPlayedCard(new Card("No effects", new ArrayList<>()));
        mockActionRequest.action = action;

        GameState result = gameService.executeAction(mockActionRequest);

        assertNotNull(result);
        assertEquals("test-game", result.getGameId());
        assertEquals("player1", result.getCurrentPlayer());
    }

    @Test
    void testExecuteActionNotAllowed() {
//        mockActionRequest.action = new ChooseCardToDiscardAction(true, "Some Card");
        ChooseCardToDiscardAction action = new ChooseCardToDiscardAction();
        action.setIsRequired(true);
        mockActionRequest.action = action;
        Exception exception = assertThrows(IllegalStateException.class, () -> gameService.executeAction(mockActionRequest));

        String expectedMessage = "Action not allowed";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testExecuteActionWithPendingAction() {
        mockState.getPendingActions().add(new PendingAction("player1", new PlayCardAction()));
        PlayCardAction action = new PlayCardAction();
        action.setPlayedCard(new Card("No effects", new ArrayList<>()));
        mockActionRequest.action = action;

        GameState result = gameService.executeAction(mockActionRequest);

        assertNotNull(result);
        assertEquals("test-game", result.getGameId());
        assertEquals("player1", result.getCurrentPlayer());
        assertTrue(mockState.getPendingActions().isEmpty());
    }

    @Test
    void testIsActionAllowedWithPendingAction() {
        mockState.getPendingActions().add(new PendingAction("player1", new PlayCardAction()));

        boolean allowed = gameService.isActionAllowed(mockState, mockActionRequest);
        assertTrue(allowed);
    }

    @Test
    void testIsActionNotAllowedWithWrongPlayer() {
        mockActionRequest.playerId = "player2";

        boolean allowed = gameService.isActionAllowed(mockState, mockActionRequest);
        assertFalse(allowed);
    }

    @Test
    void testUpdatePlayerActions() {
        gameService.updatePlayerActions(mockState, mockActionRequest);
        List<Action> availableActions = mockState.getPlayerAvailableActions().get("player1");

        assertNotNull(availableActions);
        assertEquals(1, availableActions.size());
        assertTrue(availableActions.stream().anyMatch(action -> action instanceof EndTurnAction));
    }
}