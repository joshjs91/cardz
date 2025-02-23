package com.joshjs.gamangine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.action.EndTurnAction;
import com.joshjs.gamangine.action.PlayCardAction;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.condition.CardsAllPlayedCondition;
import com.joshjs.gamangine.condition.Condition;
import com.joshjs.gamangine.model.dto.GameSetupRequest;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerIntegrationTest {

	@Autowired
	private TestRestTemplate template;

    @Test
    public void testBasicGameFlow() {
        GameState gameState = startNewGame(List.of("player1"), new CardsAllPlayedCondition());
        assertThat(gameState.getGameId()).isNotNull();
        //TODO convert action type to be an actual object to provide input
        for (String player : gameState.getPlayerHands().keySet()) {
            for (Card card : gameState.getPlayerHands().get(player)) {
                PlayCardAction action = new PlayCardAction();
                action.setPlayedCard(new Card(card.getName(), card.getEffects()));
                runAction(gameState.getGameId(), player, action);
                runAction(gameState.getGameId(), player, new EndTurnAction());
            }
        }
        GameState finalGameState = getGame(gameState.getGameId());
        assertThat(finalGameState.isGameEnded()).isEqualTo(true);
    }

    private GameState startNewGame(List<String> players, Condition gameEndingCondition) {
        // Create headers to set the Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the GameSetupRequest
        GameSetupRequest gameSetupRequest = new GameSetupRequest(players, gameEndingCondition, new HashMap<>(), "noEffectsCards");

        // Use Jackson's ObjectMapper to easily serialize the object and print it
        try {
            String requestBody = new ObjectMapper().writeValueAsString(gameSetupRequest);  // Serialize object to JSON
            System.out.println("Request Body: " + requestBody);  // Print the request body to the console
        } catch (Exception e) {
            System.err.println("Error serializing request body: " + e.getMessage());
        }

        // Create an HttpEntity with the GameSetupRequest and headers
        HttpEntity<GameSetupRequest> newGameRequest = new HttpEntity<>(gameSetupRequest, headers);

        // Perform a POST request with the appropriate headers
        ResponseEntity<GameState> response = template.exchange("/game/start", HttpMethod.POST, newGameRequest, GameState.class);
        System.out.println("Response: " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    private GameState getGame(String gameId) {
        // Create headers to set the Content-Type
        // Create an empty HttpEntity with the headers

        // Perform a POST request with the appropriate headers
        ResponseEntity<GameState> response = template.getForEntity("/game/state/" + gameId, GameState.class);
        System.out.println("Response: " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    private GameState runAction(String gameId, String player, Action action) {
        // Create headers to set the Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PlayerActionRequest> playerActionRequest = new HttpEntity<>(new PlayerActionRequest(gameId, player, action), headers);

        // Use Jackson's ObjectMapper to easily serialize the object and print it
        try {
            String requestBody = new ObjectMapper().writeValueAsString(playerActionRequest);  // Serialize object to JSON
            System.out.println("Request Body: " + requestBody);  // Print the request body to the console
        } catch (Exception e) {
            System.err.println("Error serializing request body: " + e.getMessage());
        }
        // Perform a POST request with the appropriate headers
        ResponseEntity<GameState> response = template.exchange("/game/action", HttpMethod.POST, playerActionRequest, GameState.class);

        System.out.println("Response: " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }
}
