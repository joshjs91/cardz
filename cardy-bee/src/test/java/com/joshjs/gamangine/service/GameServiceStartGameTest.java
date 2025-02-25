package com.joshjs.gamangine.service;

import com.joshjs.gamangine.model.dto.GameSetupRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceStartGameTest {
    private final GameService gameService = new GameService();

    @Test
    void testStartGameWithValidRequest() {
        GameSetupRequest request = new GameSetupRequest(
                Arrays.asList("player1", "player2"),
                null,
                Map.of("attribute1", 10),
                "defaultDeck",
                Map.of()
        );

        GameState gameState = gameService.startGame(request);

        assertNotNull(gameState);
        assertEquals(2, gameState.getPlayers().size());
        assertEquals("player1", gameState.getCurrentPlayer());
        assertEquals(10, gameState.getGameAttributes().get("attribute1"));
        assertEquals(5, gameState.getPlayerHands().get("player1").size());
        assertEquals(5, gameState.getPlayerHands().get("player2").size());
        assertTrue(gameState.getDrawDeck().size() > 0);
    }

    @Test
    void testStartGameWithEmptyPlayerIds() {
        GameSetupRequest request = new GameSetupRequest(
                new ArrayList<>(),
                null,
                Map.of("attribute1", 10),
                "defaultDeck",
                Map.of()
        );

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> gameService.startGame(request));
        assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }

    @Test
    void testStartGameWithCustomDeckType() {
        GameSetupRequest request = new GameSetupRequest(
                Arrays.asList("player1", "player2"),
                null,
                new HashMap<>(),
                "complex",
                Map.of()
        );

        GameState gameState = gameService.startGame(request);

        assertNotNull(gameState);
        assertEquals("complex", request.getDeckType());
        assertTrue(gameState.getDrawDeck().size() > 0);
    }

    @Test
    void testStartGameInitializesPendingActions() {
        GameSetupRequest request = new GameSetupRequest(
                Arrays.asList("player1", "player2"),
                null,
                new HashMap<>(),
                "defaultDeck",
                Map.of()
        );

        GameState gameState = gameService.startGame(request);

        assertNotNull(gameState);
        assertNotNull(gameState.getPendingActions());
        assertEquals(0, gameState.getPendingActions().size());
    }

    @Test
    void testStartGameInitializesPlayerAttributes() {
        GameSetupRequest request = new GameSetupRequest(
                Arrays.asList("player1", "player2"),
                null,
                new HashMap<>(),
                "defaultDeck",
                Map.of()
        );

        GameState gameState = gameService.startGame(request);

        assertNotNull(gameState);
        assertNotNull(gameState.getPlayerAttributes());
        assertTrue(gameState.getPlayerAttributes().containsKey("player1"));
        assertTrue(gameState.getPlayerAttributes().containsKey("player2"));
    }

    @Test
    void testStartGameSetsAvailableActions() {
        GameSetupRequest request = new GameSetupRequest(
                Arrays.asList("player1", "player2"),
                null,
                new HashMap<>(),
                "defaultDeck",
                Map.of()
        );

        GameState gameState = gameService.startGame(request);

        assertNotNull(gameState);
        assertNotNull(gameState.getPlayerAvailableActions().get("player1"));
        assertTrue(gameState.getPlayerAvailableActions().get("player1").size() > 0);
        assertEquals(0, gameState.getPlayerAvailableActions().get("player2").size());
    }
}