package com.joshjs.gamangine.service;

import com.joshjs.gamangine.action.*;
import com.joshjs.gamangine.action.handlers.ChooseCardToDiscardHandler;
import com.joshjs.gamangine.action.handlers.EndTurnActionHandler;
import com.joshjs.gamangine.action.handlers.PlayCardActionHandler;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.DiscardCardEffect;
import com.joshjs.gamangine.card.ModifyGameNumberAttributeEffect;
import com.joshjs.gamangine.model.dto.GameSetupRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public
class GameService {
    //These are all repositories of sorts
    private final Map<String, GameState> games = new HashMap<>();
    private final Map<String, Card> cards;
    private final Map<String, PlayerAction> actions;
    //TODO is there a list of allowed actions here too?

    public GameService() {
        this.cards = generateDefaultCards();
        this.actions = new HashMap<>();
        PlayerAction play_card = new PlayerAction("play_card", new PlayCardActionHandler());
        PlayerAction choose_discard = new PlayerAction("choose_discard", new ChooseCardToDiscardHandler());
        PlayerAction end_turn = new PlayerAction("end_turn", new EndTurnActionHandler());
        this.actions.put("play_card", play_card);
        this.actions.put("choose_discard", choose_discard);
        this.actions.put("end_turn", end_turn);
    }

    public GameState startGame(GameSetupRequest request) {
        String gameId = UUID.randomUUID().toString();
        List<String> playerIds = request.getPlayerIds();
        String currentPlayer = playerIds.get(0);
        Map<String, Object> gameAttributes = request.getGameAttributes();
        HashMap<String, List<PlayerAction>> playerAvailableActions = new HashMap<>();
        LinkedList<PendingAction> pendingActions = new LinkedList<>();

        List<Card> deck = generateDeck(request.getDeckType());
        ArrayList<Card> drawDeck = new ArrayList<>(deck);
        ArrayList<Card> discardPile = new ArrayList<>();
        HashMap<String, List<Card>> playerHands = new HashMap<>();
        GameState state = new GameState(
                gameId,
                playerIds,
                currentPlayer,
                gameAttributes,
                playerAvailableActions,
                pendingActions,
                drawDeck,
                discardPile,
                playerHands,
                request.getGameEndedCondition()
        );

        for (String player : playerIds) {
            state.getPlayerHands().put(player, drawCards(state.getDrawDeck(), 5));
            if (player == state.getCurrentPlayer()) {
                state.getPlayerAvailableActions().put(state.getCurrentPlayer(), actions.values().stream().toList());
            } else {
                state.getPlayerAvailableActions().put(player, new ArrayList<>());
            }
        }

        System.out.println("Game started with ID: " + gameId);
        games.put(gameId, state);
        return state;
    }

    private List<Card> drawCards(List<Card> deck, int count) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < count && !deck.isEmpty(); i++) {
            drawnCards.add(deck.remove(0));
        }
        return drawnCards;
    }

    public GameState executeAction(PlayerActionRequest action) {
        GameState state = games.get(action.gameId);
        if (state == null) throw new IllegalArgumentException("Invalid game ID");

        System.out.println("Player " + action.playerId + " is performing action: " + action.actionType);

        // Validate if action is allowed based on pending actions
        if (!state.getPendingActions().isEmpty()) {
            PendingAction pending = state.getPendingActions().peek();
            if (!pending.getPlayer().equals(action.playerId) || !pending.getAction().actionType.equals(action.actionType)) {
                throw new IllegalStateException("Action not allowed at this time");
            }
            executeAction(state, action);
            state.getPendingActions().poll();
        } else {
            // Execute the action
            executeAction(state, action);
        }


        return state;
    }

    private void executeAction(GameState state, PlayerActionRequest action) {
        // Find the first available action that matches the action type
        //This just needs to check its the actual players turn
        if (!state.getCurrentPlayer().equals(action.playerId)) {
            throw new IllegalStateException("Its not your turn you FOOL");
        }
        Optional<PlayerAction> availableAction = state.getPlayerAvailableActions().get(action.playerId)
                .stream()
                .filter(availablePlayerAction -> availablePlayerAction.actionType.equals(action.actionType))
                .findFirst();

        if (availableAction.isPresent()) {
            PlayerAction actionToExecute = availableAction.get();
            actionToExecute.applyHandlers(state, action);

            // Remove the first instance of the action from the list
            List<PlayerAction> playerActions = state.getPlayerAvailableActions().get(action.playerId);

            // Create a new list excluding the first matching action
            List<PlayerAction> updatedActions = playerActions.stream()
                    .filter(playerAction -> !playerAction.equals(actionToExecute) || playerActions.indexOf(playerAction) != playerActions.indexOf(actionToExecute))
                    .collect(Collectors.toList());

            // Update the player's available actions in the state
            state.getPlayerAvailableActions().put(action.playerId, updatedActions);
        } else {
            throw new IllegalStateException("Player action not available: " + action.actionType);
        }
    }

//TODO turn into a factory
    private Map<String, Card> generateDefaultCards() {
        Map<String, Card> defaultCards = new HashMap<>();
        Map<String, Class<?>> cardRequiredInputs = new HashMap<>();
        defaultCards.put("Remove tokens", new Card("Remove tokens", List.of(new ModifyGameNumberAttributeEffect()), cardRequiredInputs));
        defaultCards.put("Card1", new Card("Card1", List.of(), cardRequiredInputs));
        defaultCards.put("SuperDuper", new Card("SuperDuper", List.of(), cardRequiredInputs));
        defaultCards.put("SuperDuper pooper", new Card("SuperDuper", List.of(), cardRequiredInputs));
        defaultCards.put("No effects", new Card("No effects", List.of(), cardRequiredInputs));
        return defaultCards;
    }

    private Map<String, Card> generateComplexCards() {
        Map<String, Card> defaultCards = new HashMap<>();
        //TODO these validations are wrong
        Map<String, Class<?>> cardRequiredInputs = new HashMap<>();
        cardRequiredInputs.put("discardCardTargetPlayer", String.class);
        cardRequiredInputs.put("modifyAttributeTargetPlayer", String.class);
        defaultCards.put("Card1", new Card("Card1", List.of(new DiscardCardEffect(), new ModifyGameNumberAttributeEffect()), cardRequiredInputs));
        defaultCards.put("SuperDuper", new Card("SuperDuper", List.of(new ModifyGameNumberAttributeEffect()), cardRequiredInputs));
        return defaultCards;
    }

    public GameState getGameState(String gameId) {
        return games.get(gameId);
    }

    private List<Card> generateDeck(String deckType) {
        List<Card> defaultDeck = generateDefaultCards().values().stream().toList();

        if (deckType == null) {
            return defaultDeck;
        }

        return switch (deckType) {
            case "tokenCards" -> generateDefaultCards().values().stream().filter(card -> card.getName().contains("tokens")).toList();
            case "complex" -> generateComplexCards().values().stream().toList();
            case "noEffectsCards" -> generateDefaultCards().values().stream().filter(card -> card.getName().contains("No effects")).toList();
            default -> defaultDeck;
        };
    }
}
