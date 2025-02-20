package com.joshjs.gamangine;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                action.setCardName(card.getName());
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

        // Create an empty HttpEntity with the headers
        HttpEntity<GameSetupRequest> newGameRequest = new HttpEntity<>(new GameSetupRequest(players, gameEndingCondition, new HashMap<>(), "noEffectsCards"), headers);

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

        // Perform a POST request with the appropriate headers
        ResponseEntity<GameState> response = template.exchange("/game/action", HttpMethod.POST, playerActionRequest, GameState.class);

        System.out.println("Response: " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }
}
