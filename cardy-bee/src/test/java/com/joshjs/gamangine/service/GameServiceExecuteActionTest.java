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

class GameServiceExecuteActionTest {
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    private GameState createNewGame(String gameId) {
        GameState state = new GameState();
        state.setGameId(gameId);
        state.setPlayers(Arrays.asList("player1", "player2"));
        state.setCurrentPlayer("player1");
        state.setPlayerAvailableActions(new HashMap<>());
        state.setDiscardPile(new ArrayList<>());

        Map<String, List<Card>> playerHands = new HashMap<>();
        for (String player : state.getPlayers()) {
            List<Card> hand = new ArrayList<>();
            hand.add(new Card("No effects", new ArrayList<>()));
            playerHands.put(player, hand);
        }
        state.setPlayerHands(playerHands);

        List<Action> actions = new ArrayList<>();
        actions.add(new PlayCardAction());
        BaseAction action = new EndTurnAction();
        action.setIsRequired(true);
        actions.add(action);
        state.getPlayerAvailableActions().put("player1", actions);

        gameService.games.put(gameId, state);
        return state;
    }

    private PlayerActionRequest createActionRequest(String gameId, String playerId, Action action) {
        PlayerActionRequest request = new PlayerActionRequest();
        request.gameId = gameId;
        request.playerId = playerId;
        request.action = action;
        return request;
    }

    @Test
    void testExecuteActionAllowed() {
        GameState state = createNewGame("test-game-1");
        PlayCardAction action = new PlayCardAction();
        action.setPlayedCard(new Card("No effects", new ArrayList<>()));
        PlayerActionRequest request = createActionRequest("test-game-1", "player1", action);

        GameState result = gameService.executeAction(request);

        assertNotNull(result);
        assertEquals("test-game-1", result.getGameId());
        assertEquals("player1", result.getCurrentPlayer());
    }

    @Test
    void testExecuteActionNotAllowed() {
        GameState state = createNewGame("test-game-2");
        DiscardCardAction action = new DiscardCardAction();
        action.setIsRequired(true);
        PlayerActionRequest request = createActionRequest("test-game-2", "player1", action);

        Exception exception = assertThrows(IllegalStateException.class, () -> gameService.executeAction(request));

        String expectedMessage = "Action not allowed";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testExecuteActionWithPendingAction() {
        GameState state = createNewGame("test-game-3");
        state.getPendingActions().add(new PendingAction("player1", new PlayCardAction()));

        PlayCardAction action = new PlayCardAction();
        action.setPlayedCard(new Card("No effects", new ArrayList<>()));
        PlayerActionRequest request = createActionRequest("test-game-3", "player1", action);

        GameState result = gameService.executeAction(request);

        assertNotNull(result);
        assertEquals("test-game-3", result.getGameId());
        assertEquals("player1", result.getCurrentPlayer());
        assertTrue(state.getPendingActions().isEmpty());
    }

    @Test
    void testIsActionAllowedWithPendingAction() {
        GameState state = createNewGame("test-game-4");
        state.getPendingActions().add(new PendingAction("player1", new PlayCardAction()));

        PlayerActionRequest request = createActionRequest("test-game-4", "player1", new PlayCardAction());
        boolean allowed = gameService.isActionAllowed(state, request);
        assertTrue(allowed);
    }

    @Test
    void testIsActionNotAllowedWithWrongPlayer() {
        GameState state = createNewGame("test-game-5");

        PlayerActionRequest request = createActionRequest("test-game-5", "player2", new PlayCardAction());
        boolean allowed = gameService.isActionAllowed(state, request);
        assertFalse(allowed);
    }

    @Test
    void testUpdatePlayerActions() {
        GameState state = createNewGame("test-game-6");

        PlayerActionRequest request = createActionRequest("test-game-6", "player1", new PlayCardAction());
        gameService.updatePlayerActions(state, request);
        List<Action> availableActions = state.getPlayerAvailableActions().get("player1");

        assertNotNull(availableActions);
        assertEquals(1, availableActions.size());
        assertTrue(availableActions.stream().anyMatch(action -> action instanceof EndTurnAction));
    }

    @Test
    void testIsActionAllowedWhenNoPendingActionsAndInAllowedList() {
        GameState state = createNewGame("test-game-7");

        PlayCardAction action = new PlayCardAction();
        action.setPlayedCard(new Card("No effects", new ArrayList<>()));
        PlayerActionRequest request = createActionRequest("test-game-7", "player1", action);

        // Ensure no pending actions exist
        assertTrue(state.getPendingActions().isEmpty());

        // Check if the action is allowed based on available actions
        boolean allowed = gameService.isActionAllowed(state, request);
        assertTrue(allowed, "Action should be allowed when there are no pending actions and it is within the allowed actions list.");
    }

    @Test
    void testActionNotAllowedWhenPendingActionsExist() {
        // Create a new game state
        GameState state = createNewGame("test-game-8");

        // Add a pending action for a different action type (e.g., EndTurnAction)
        state.getPendingActions().add(new PendingAction("player1", new EndTurnAction()));

        // Create a PlayCardAction as the user's action
        PlayCardAction action = new PlayCardAction();
        action.setPlayedCard(new Card("No effects", new ArrayList<>()));
        PlayerActionRequest request = createActionRequest("test-game-8", "player1", action);

        // Check if the action is not allowed due to pending actions
        boolean allowed = gameService.isActionAllowed(state, request);
        assertFalse(allowed, "Action should not be allowed when there are pending actions that do not match.");
    }

    @Test
    void testActionAllowedWhenPendingActionMatches() {
        GameState state = createNewGame("test-game-9");

        // Add a matching pending action
        PlayCardAction pendingAction = new PlayCardAction();
        state.getPendingActions().add(new PendingAction("player1", pendingAction));

        // Create a matching PlayCardAction
        PlayCardAction action = new PlayCardAction();
        action.setPlayedCard(new Card("No effects", new ArrayList<>()));
        PlayerActionRequest request = createActionRequest("test-game-9", "player1", action);

        // Check if the action is allowed when it matches the pending action
        boolean allowed = gameService.isActionAllowed(state, request);
        assertTrue(allowed, "Action should be allowed when it matches the pending action.");
    }

    @Test
    void testActionNotAllowedWhenPerformedByWrongPlayer() {
        GameState state = createNewGame("test-game-10");

        // Add a pending action for player1
        state.getPendingActions().add(new PendingAction("player1", new EndTurnAction()));

        // Create an action performed by player2
        PlayCardAction action = new PlayCardAction();
        action.setPlayedCard(new Card("No effects", new ArrayList<>()));
        PlayerActionRequest request = createActionRequest("test-game-10", "player2", action);

        // Check if the action is not allowed when performed by the wrong player
        boolean allowed = gameService.isActionAllowed(state, request);
        assertFalse(allowed, "Action should not be allowed when performed by the wrong player.");
    }

    @Test
    void testActionNotAllowedWhenNotInPlayerAvailableActions() {
        GameState state = createNewGame("test-game-12");

        // Remove all actions from player's available actions
        state.getPlayerAvailableActions().get("player1").clear();

        // Create an action
        PlayCardAction action = new PlayCardAction();
        action.setPlayedCard(new Card("No effects", new ArrayList<>()));
        PlayerActionRequest request = createActionRequest("test-game-12", "player1", action);

        // Check if the action is not allowed when it's not in the available actions list
        boolean allowed = gameService.isActionAllowed(state, request);
        assertFalse(allowed, "Action should not be allowed when it is not in the player's available actions list.");
    }


}