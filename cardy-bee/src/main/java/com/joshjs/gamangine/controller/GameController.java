package com.joshjs.gamangine.controller;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public GameState startGame(@RequestBody GameSetupRequest request) {
        return gameService.startGame(request);
    }

    @PostMapping("/action")
    public GameState performAction(@RequestBody PlayerAction action) {
        return gameService.performAction(action);
    }

    @GetMapping("/state/{gameId}")
    public GameState getGameState(@PathVariable String gameId) {
        return gameService.getGameState(gameId);
    }

    @GetMapping("/")
    public String home() {
        return "Greetings!";
    }
}

class GameSetupRequest {
    public List<String> playerIds;
}

class PlayerAction {
    public String gameId;
    public String playerId;
    public String actionType;
    public Map<String, Object> actionData;
}

class GameState {
    public String gameId;
    public List<String> players;
    public String currentPlayer;
    public Map<String, Object> gameAttributes;

    public List<String> availableActions;
    public Map<String, List<String>> playerAvailableActions;
    public Queue<PlayerAction> pendingActions = new LinkedList<>();

    public List<Card> drawDeck = new ArrayList<>();
    public List<Card> discardPile = new ArrayList<>();
    public Map<String, List<Card>> playerHands = new HashMap<>();
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DiscardCardEffect.class, name = "DiscardCardEffect"),
        @JsonSubTypes.Type(value = ModifyAttributeEffect.class, name = "ModifyAttributeEffect")
})
interface CardEffect {
    void applyEffect(GameState state, PlayerAction action);
}

class DiscardCardEffect implements CardEffect {
    @Override
    public void applyEffect(GameState state, PlayerAction action) {
        String targetPlayer = (String) action.actionData.get("targetPlayer");

        System.out.println("Player " + targetPlayer + " must discard a card.");

        PlayerAction pendingAction = new PlayerAction();
        pendingAction.gameId = action.gameId;
        pendingAction.playerId = targetPlayer;
        pendingAction.actionType = "choose_discard";
        state.pendingActions.add(pendingAction);
    }
}

class ModifyAttributeEffect implements CardEffect {
    @Override
    public void applyEffect(GameState state, PlayerAction action) {
        String targetPlayer = (String) action.actionData.get("targetPlayer");
        String attribute = (String) action.actionData.get("attribute");
        int valueChange = (int) action.actionData.get("valueChange");

        state.gameAttributes.put(attribute, (int) state.gameAttributes.getOrDefault(attribute, 0) + valueChange);
        System.out.println("Player " + targetPlayer + "'s attribute " + attribute + " changed by " + valueChange);
    }
}

class Card {
    public String name;
    public List<CardEffect> effects;

    public Card(String name, List<CardEffect> effects) {
        this.name = name;
        this.effects = effects;
    }

    public void applyEffects(GameState state, PlayerAction action) {
        System.out.println("Player " + action.playerId + " played card: " + name);
        for (CardEffect effect : effects) {
            effect.applyEffect(state, action);
        }
    }
}

@Service
class GameService {
    private final Map<String, GameState> games = new HashMap<>();
    private final Map<String, Card> cards;
    private final Map<String, GameAction> actionHandlers;

    public GameService() {
        this.cards = generateDefaultCards();
        this.actionHandlers = new HashMap<>();
        actionHandlers.put("play_card", new PlayCardAction(cards));
        actionHandlers.put("end_turn", new EndTurnAction());
    }

    public GameState startGame(GameSetupRequest request) {
        String gameId = UUID.randomUUID().toString();
        GameState state = new GameState();
        state.gameId = gameId;
        state.players = request.playerIds;
        state.currentPlayer = request.playerIds.get(0);
        state.gameAttributes = new HashMap<>();
        state.availableActions = List.of("play_card", "end_turn", "use_skill");
        state.playerAvailableActions = new HashMap<>();
        state.drawDeck = new ArrayList<>(generateDeck());
        state.discardPile = new ArrayList<>();
        state.playerHands = new HashMap<>();

        for (String player : request.playerIds) {
            state.playerAvailableActions.put(player, new ArrayList<>(List.of("play_card", "use_skill")));
            state.playerHands.put(player, drawCards(state.drawDeck, 5));
        }

        System.out.println("Game started with ID: " + gameId);
        games.put(gameId, state);

        //TODO print out the cards i have

        return state;
    }

    private List<Card> drawCards(List<Card> deck, int count) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < count && !deck.isEmpty(); i++) {
            drawnCards.add(deck.remove(0));
        }
        return drawnCards;
    }

    public GameState performAction(PlayerAction action) {
        GameState state = games.get(action.gameId);
        if (state == null) throw new IllegalArgumentException("Invalid game ID");

        System.out.println("Player " + action.playerId + " is performing action: " + action.actionType);

        // Validate if action is allowed based on pending actions
        if (!state.pendingActions.isEmpty()) {
            PlayerAction pending = state.pendingActions.peek();
            if (!pending.playerId.equals(action.playerId) || !pending.actionType.equals(action.actionType)) {
                throw new IllegalStateException("Action not allowed at this time");
            }
        }

        // Execute the action
        GameAction gameAction = actionHandlers.get(action.actionType);
        if (gameAction != null) {
            gameAction.execute(state, action);
        } else {
            throw new IllegalStateException("Unknown action type: " + action.actionType);
        }

        // If it was a pending action, remove it now
        if (!state.pendingActions.isEmpty()) {
            state.pendingActions.poll();
        }

        return state;
    }


    private Map<String, Card> generateDefaultCards() {
        Map<String, Card> defaultCards = new HashMap<>();
        defaultCards.put("Card1", new Card("Card1", List.of(new DiscardCardEffect(), new ModifyAttributeEffect())));
        defaultCards.put("SuperDuper", new Card("SuperDuper", List.of(new ModifyAttributeEffect())));
        return defaultCards;
    }

    public GameState getGameState(String gameId) {
        return games.get(gameId);
    }

    private List<Card> generateDeck() {
        return new ArrayList<>(cards.values());
    }
}

interface GameAction {
    void execute(GameState state, PlayerAction action);
}

class PlayCardAction implements GameAction {
    private final Map<String, Card> cards;

    public PlayCardAction(Map<String, Card> cards) {
        this.cards = cards;
    }

    @Override
    public void execute(GameState state, PlayerAction action) {
        String cardName = (String) action.actionData.get("cardName");
        List<Card> playerHand = state.playerHands.get(action.playerId);

        if (playerHand != null && playerHand.removeIf(card -> card.name.equals(cardName))) {
            Card card = cards.get(cardName);
            if (card != null) {
                card.applyEffects(state, action);
                state.discardPile.add(card);
            }
        } else {
            throw new IllegalStateException("Player does not have the specified card");
        }
    }
}

class EndTurnAction implements GameAction {
    @Override
    public void execute(GameState state, PlayerAction action) {
        if (!state.currentPlayer.equals(action.playerId)) {
            throw new IllegalStateException("Only the current player can end their turn");
        }

        updateTurn(state);
    }

    private void updateTurn(GameState state) {
        if (state.pendingActions.isEmpty()) {
            int currentIndex = state.players.indexOf(state.currentPlayer);
            state.currentPlayer = state.players.get((currentIndex + 1) % state.players.size());

            // Assign available actions for the new player
            state.availableActions = List.of("play_card", "end_turn", "use_skill");
            state.playerAvailableActions.put(state.currentPlayer, new ArrayList<>(state.availableActions));

            System.out.println("Turn changed to player: " + state.currentPlayer);
        }
    }

}