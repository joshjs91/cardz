package com.joshjs.gamangine.service;

import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.action.ChooseCardToDiscardAction;
import com.joshjs.gamangine.action.EndTurnAction;
import com.joshjs.gamangine.action.PlayCardAction;
import com.joshjs.gamangine.action.model.PendingAction;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.effects.DiscardCardEffect;
import com.joshjs.gamangine.card.effects.ModifyGameNumberAttributeEffect;
import com.joshjs.gamangine.model.dto.GameSetupRequest;
import com.joshjs.gamangine.model.state.GameState;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public
class GameService {
    //These are all repositories of sorts
    //TODO turn this into a repository......
    final Map<String, GameState> games = new HashMap<>();
    private final Map<String, Card> cards;
    private final Map<String, Action> actions;
    //TODO is there a list of allowed actions here too?

    public GameService() {
        this.cards = generateDefaultCards();
        this.actions = new HashMap<>();
        Action play_card = new PlayCardAction();
        Action choose_discard = new ChooseCardToDiscardAction();
        Action end_turn = new EndTurnAction();
        this.actions.put("play_card", play_card);
        this.actions.put("choose_discard", choose_discard);
        this.actions.put("end_turn", end_turn);
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
                request.getGameEndedCondition(),
                new ArrayList<>(),
                new ArrayList<>()
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

    public GameState executeAction(PlayerActionRequest actionRequest) {
        GameState state = games.get(actionRequest.gameId);
        if (state == null) {
            throw new IllegalArgumentException("Invalid game ID");
        }

        System.out.println("Player " + actionRequest.playerId + " is performing action: " + actionRequest.action);

        // Validate and execute the action
        if (isActionAllowed(state, actionRequest)) {
            actionRequest.action.execute(state, actionRequest);

            // Remove pending action if it exists
            if (!state.getPendingActions().isEmpty()) {
                PendingAction pending = state.getPendingActions().peek();
                if (playerMatchesNextPendingActionPlayer(pending.getPlayer(), actionRequest) &&
                        actionMatchesNextPendingAction(pending.getAction(), actionRequest.getAction())) {
                    state.getPendingActions().poll();
                }
            }

            // Update the player's available actions in the state
            updatePlayerActions(state, actionRequest);
        } else {
            throw new IllegalStateException("Action not allowed: " + actionRequest.action);
        }

        return state;
    }

    public boolean isActionAllowed(GameState state, PlayerActionRequest actionRequest) {
        // Check if the action is the next pending action
        if (!state.getPendingActions().isEmpty()) {
            PendingAction pending = state.getPendingActions().peek();
            if (!playerMatchesNextPendingActionPlayer(pending.getPlayer(), actionRequest) || !actionMatchesNextPendingAction(pending.getAction(), actionRequest.getAction())) {
                return false;
            }
        }

        // Check if the action is allowed for the player
        return state.getPlayerAvailableActions().getOrDefault(actionRequest.playerId, Collections.emptyList())
                .stream()
                .anyMatch(availablePlayerAction -> actionMatchesNextPendingAction(availablePlayerAction, actionRequest.getAction()));
    }

    private boolean actionMatchesNextPendingAction(Action pending, Action actionRequest) {
        return pending.getClass().equals(actionRequest.getClass());
    }

    private boolean playerMatchesNextPendingActionPlayer(String nextPendingActionPlayer, PlayerActionRequest actionRequest) {
        return nextPendingActionPlayer.equals(actionRequest.playerId);
    }

    public void updatePlayerActions(GameState state, PlayerActionRequest actionRequest) {
        List<Action> updatedActions = new ArrayList<>(state.getPlayerAvailableActions().get(actionRequest.playerId));
        updatedActions.removeIf(action -> actionMatchesNextPendingAction(action, actionRequest.getAction()));
        state.getPlayerAvailableActions().put(actionRequest.playerId, updatedActions);
    }

    //TODO turn into a factory
    private Map<String, Card> generateDefaultCards() {
        Map<String, Card> defaultCards = new HashMap<>();
        Map<String, Class<?>> cardRequiredInputs = new HashMap<>();
        defaultCards.put("Remove tokens", new Card("Remove tokens", List.of(new ModifyGameNumberAttributeEffect())));
        defaultCards.put("Card1", new Card("Card1", List.of()));
        defaultCards.put("SuperDuper", new Card("SuperDuper", List.of()));
        defaultCards.put("SuperDuper pooper", new Card("SuperDuper", List.of()));
        defaultCards.put("No effects", new Card("No effects", List.of()));
        return defaultCards;
    }

    private Map<String, Card> generateComplexCards() {
        Map<String, Card> defaultCards = new HashMap<>();
        //TODO these validations are wrong
        Map<String, Class<?>> cardRequiredInputs = new HashMap<>();
        cardRequiredInputs.put("discardCardTargetPlayer", String.class);
        cardRequiredInputs.put("modifyAttributeTargetPlayer", String.class);
        defaultCards.put("Card1", new Card("Card1", List.of(new DiscardCardEffect(), new ModifyGameNumberAttributeEffect())));
        defaultCards.put("SuperDuper", new Card("SuperDuper", List.of(new ModifyGameNumberAttributeEffect())));
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
