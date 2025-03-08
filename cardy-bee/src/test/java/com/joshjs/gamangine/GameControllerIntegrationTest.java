package com.joshjs.gamangine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.action.EndTurnAction;
import com.joshjs.gamangine.action.PlayCardAction;
import com.joshjs.gamangine.action.spanish41.DrawCardAction;
import com.joshjs.gamangine.action.spanish41.PlaySpanish41CardAction;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41BaseEffect;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41DrawCardEffect;
import com.joshjs.gamangine.condition.AllPlayersHaveNoCardsInHandCondition;
import com.joshjs.gamangine.condition.Condition;
import com.joshjs.gamangine.condition.PlayerHasNoCardsInHandCondition;
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

import static com.joshjs.gamangine.factory.DeckFactory.generateSimpleSpanish41Deck;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerIntegrationTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void testBasicGameFlow() {
        List<String> players = new ArrayList<>();
        players.add("player 1");
        players.add("player 2");
        GameState gameState = startNewGame(players, new AllPlayersHaveNoCardsInHandCondition(), "noEffectsCards", "default");
        assertThat(gameState.getGameId()).isNotNull();
        //TODO convert action type to be an actual object to provide input
        while (!gameState.isGameEnded()) {
            // for each player play a card and then end their turn.
            for (String player : players) {
                Card card = gameState.getPlayerHands().get(player).get(0);
                PlayCardAction action = new PlayCardAction();
                action.setPlayedCard(new Card(card.getName(), card.getEffects()));
                runAction(gameState.getGameId(), player, action);

                runAction(gameState.getGameId(), player, new EndTurnAction());
            }
            gameState = getGame(gameState.getGameId());
        }
        GameState finalGameState = getGame(gameState.getGameId());
        assertThat(finalGameState.isGameEnded()).isEqualTo(true);
    }
    @Test
    public void testSpanish41Actions() {
        // Initialize players
        String player1 = "player 1";
        String player2 = "player 2";
        List<String> players = List.of(player1, "player 2");

        // Start game with generated deck
        GameState gameState = startNewGame(players, new PlayerHasNoCardsInHandCondition(), "simpleSpanish41", "spanish41");
        assertThat(gameState.getGameId()).isNotNull();

        // Players draw 5 cards each (from reverse deck order)
        gameState = getGame(gameState.getGameId());
        List<Card> cards = generateSimpleSpanish41Deck();
        // Player 1's Hand (reverse draw order from generateSimpleSpanish41Deck)
        Card card1_p1 = cards.get(0);
        // - "1 blue": { colour: "blue", number: 1 }

        // Player 1 plays a card
        playSpanish41Card(gameState, player1, "blue", 1);
        playDraw2Card(player2, gameState);
        playDraw2Card(player1, gameState);
//        TODO put something to check an error to make sure they have to draw 4 cards here
//        TODO put in something to check that it doesnt let me play a draw x card without a colour
        drawXCards(player2, gameState, 4);
        //TODO check that 4 cards get added to the players hand here too
        playSpanish41Card(gameState, player1, "green", 1);
        playSpanish41Card(gameState, player2, "green", 2);
        playSpanish41Card(gameState, player1, "yellow", 2);
        playSpanish41Card(gameState, player2, "red", 2);
        playSpanish41Card(gameState, player1, "red", 2);

        // Check final game state
        GameState finalGameState = getGame(gameState.getGameId());
        assertThat(finalGameState.isGameEnded()).isEqualTo(true);
    }

    private void drawXCards(String player2, GameState gameState, int cardsToDraw) {
        DrawCardAction action = new DrawCardAction();
        action.setCardsToDraw(cardsToDraw);
        runAction(gameState.getGameId(), player2, action);
    }

    private void playDraw2Card(String player1, GameState gameState) {
        PlaySpanish41CardAction action9 = new PlaySpanish41CardAction();
        Spanish41DrawCardEffect drawEffect = new Spanish41DrawCardEffect();
        drawEffect.setCardsToDraw(2);
        drawEffect.setColour("green");
        action9.setPlayedCard(new Card("Draw 2", List.of(drawEffect)));
        runAction(gameState.getGameId(), player1, action9);
    }

    private void playSpanish41Card(GameState gameState, String player, String colour, Integer number) {
        PlaySpanish41CardAction action1 = new PlaySpanish41CardAction();
        Spanish41BaseEffect effect = new Spanish41BaseEffect();
        effect.setNumber(number);
        effect.setColour(colour);
        Card card = new Card(number + " " + colour, new ArrayList<>(List.of(effect)));
        action1.setPlayedCard(card);
        runAction(gameState.getGameId(), player, action1);
    }


    private GameState startNewGame(List<String> players, Condition gameEndingCondition, String deckType, String gameType) {
        // Create headers to set the Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the GameSetupRequest
        GameSetupRequest gameSetupRequest = new GameSetupRequest(players, gameEndingCondition, new HashMap<>(), deckType, gameType, new HashMap<>());

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
        System.out.println("Response for new game: " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    private GameState getGame(String gameId) {
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

//        // Use Jackson's ObjectMapper to serialize the request and print it
//        try {
//            String requestBody = new ObjectMapper().writeValueAsString(playerActionRequest);
//            System.out.println("Request Body: " + requestBody);
//        } catch (Exception e) {
//            System.err.println("Error serializing request body: " + e.getMessage());
//        }

        // Perform a POST request and handle unexpected responses

        ResponseEntity<String> response = template.exchange("/game/action", HttpMethod.POST, playerActionRequest, String.class);

        System.out.println("Response: " + response.getStatusCode() + response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Try to deserialize the response into GameState
        try {
            return new ObjectMapper().readValue(response.getBody(), GameState.class);
        } catch (Exception e) {
            System.err.println("Error deserializing response to GameState: " + e.getMessage());
            fail("Response is not a valid GameState JSON. Raw Response: " + response.getBody());
            return null;
        }

    }

}
