package com.joshjs.gamangine;

import com.joshjs.gamangine.model.GameSetupRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerIntegrationTest {

	@Autowired
	private TestRestTemplate template;

    @Test
    public void checkBasicGameFlow() throws Exception {
        GameState gameState = startNewGame();
        assertThat(gameState.getGameId()).isNotNull();
    }

    private GameState startNewGame() {
        // Create headers to set the Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an empty HttpEntity with the headers
        HttpEntity<GameSetupRequest> newGameRequest = new HttpEntity<>(new GameSetupRequest(List.of("player1")), headers);

        // Perform a POST request with the appropriate headers
        ResponseEntity<GameState> response = template.exchange("/game/start", HttpMethod.POST, newGameRequest, GameState.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }
}
