package com.joshjs.gamangine.service;

import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.action.model.PendingAction;
import com.joshjs.gamangine.card.DeckFactory;
import com.joshjs.gamangine.exception.InvalidInputException;
import com.joshjs.gamangine.game.GameConfig;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.model.dto.GameSetupRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public
class GameService {
    //TODO turn this into a repository......
    final Map<String, GameState> games = new HashMap<>();

    public GameService() {
    }

    public GameState startGame(GameSetupRequest request) {
        String gameId = UUID.randomUUID().toString();
        List<String> playerIds = request.getPlayerIds();
        String currentPlayer = playerIds.get(0);
        Map<String, Object> gameAttributes = request.getGameAttributes();
        Map<String, List<Action>> playerAvailableActions = new HashMap<>();
        LinkedList<PendingAction> pendingActions = new LinkedList<>();

        List<Card> deck = generateDeck(request.getDeckType());
        ArrayList<Card> drawDeck = new ArrayList<>(deck);
        ArrayList<Card> discardPile = new ArrayList<>();
        HashMap<String, List<Card>> playerHands = new HashMap<>();
        HashMap<String, Map<String, Object>> playerAttributes = new HashMap<>();

        GameState state = new GameState(
                gameId,
                playerIds,
                playerAttributes,
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
            state.getPlayerAttributes().put(player, new HashMap<>());
            state.getPlayerHands().put(player, drawCards(state.getDrawDeck(), 5));
            if (player == state.getCurrentPlayer()) {
                state.getPlayerAvailableActions().put(state.getCurrentPlayer(), GameConfig.genericTurnActions());
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

    public GameState executeAction(PlayerActionRequest actionRequest) {
        GameState state = games.get(actionRequest.gameId);
        if (state == null) {
            throw new InvalidInputException("Invalid game ID");
        }

        System.out.println("Player " + actionRequest.playerId + " is performing action: " + actionRequest.action);

        // Validate and execute the action
        if (isActionAllowed(state, actionRequest)) {
            actionRequest.action.execute(state, actionRequest);

            // Remove pending action if it exists
            if (!state.getPendingActions().isEmpty()) {
                PendingAction pending = state.getPendingActions().peek();
                if (playerMatchesNextPendingActionPlayer(pending.getPlayer(), actionRequest) &&
                        actionMatchesAvailablePlayerAction(pending.getAction(), actionRequest.getAction())) {
                    state.getPendingActions().poll();
                }
            }

            // Update the player's available actions in the state
            updatePlayerActions(state, actionRequest);
        } else {
            throw new InvalidInputException("Action not allowed: " + actionRequest.action);
        }

        return state;
    }

    public boolean isActionAllowed(GameState state, PlayerActionRequest actionRequest) {
        // Check if the action is the next pending action
        if (!state.getPendingActions().isEmpty()) {
            PendingAction pending = state.getPendingActions().peek();
            if (!playerMatchesNextPendingActionPlayer(pending.getPlayer(), actionRequest) || !actionMatchesAvailablePlayerAction(pending.getAction(), actionRequest.getAction())) {
                return false;
            }
        }

        return state.getPlayerAvailableActions().getOrDefault(actionRequest.playerId, Collections.emptyList())
                .stream()
                .anyMatch(availablePlayerAction -> actionMatchesAvailablePlayerAction(availablePlayerAction, actionRequest.getAction()));
    }

    private boolean actionMatchesAvailablePlayerAction(Action allowedOrPendingAction, Action actionRequest) {
        return allowedOrPendingAction.getClass().equals(actionRequest.getClass());
    }

    private boolean playerMatchesNextPendingActionPlayer(String nextPendingActionPlayer, PlayerActionRequest actionRequest) {
        return nextPendingActionPlayer.equals(actionRequest.playerId);
    }

    public void updatePlayerActions(GameState state, PlayerActionRequest actionRequest) {
        List<Action> updatedActions = new ArrayList<>(state.getPlayerAvailableActions().get(actionRequest.playerId));
        updatedActions.removeIf(action -> actionMatchesAvailablePlayerAction(action, actionRequest.getAction()));
        state.getPlayerAvailableActions().put(actionRequest.playerId, updatedActions);
    }

    public GameState getGameState(String gameId) {
        return games.get(gameId);
    }

    private List<Card> generateDeck(String deckType) {
        return DeckFactory.generateDeck(deckType);
    }
}
