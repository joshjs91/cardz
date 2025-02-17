package com.joshjs.gamangine.service;

import com.joshjs.gamangine.action.EndTurnAction;
import com.joshjs.gamangine.action.GameAction;
import com.joshjs.gamangine.action.PlayCardAction;
import com.joshjs.gamangine.action.PlayerAction;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.DiscardCardEffect;
import com.joshjs.gamangine.card.ModifyAttributeEffect;
import com.joshjs.gamangine.model.GameSetupRequest;
import com.joshjs.gamangine.model.GameState;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public
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

    public GameState executeAction(PlayerAction action) {
        GameState state = games.get(action.gameId);
        if (state == null) throw new IllegalArgumentException("Invalid game ID");

        System.out.println("Player " + action.playerId + " is performing action: " + action.actionType);

        // Validate if action is allowed based on pending actions
        if (!state.pendingActions.isEmpty()) {
            PlayerAction pending = state.pendingActions.peek();
            System.out.println("sizeeeeee" + state.pendingActions.size());
            if (!pending.playerId.equals(action.playerId) || !pending.actionType.equals(action.actionType)) {
                throw new IllegalStateException("Action not allowed at this time");
            }
            executeAction(action, state);
            state.pendingActions.poll();
        } else {
            // Execute the action
            executeAction(action, state);
            //TODO presume i should remove this was users available actions here too
        }


        return state;
    }

    private void executeAction(PlayerAction action, GameState state) {
        GameAction gameAction = actionHandlers.get(action.actionType);
        if (gameAction != null) {
            gameAction.execute(state, action);
        } else {
            throw new IllegalStateException("Unknown action type: " + action.actionType);
        }
    }


    private Map<String, Card> generateDefaultCards() {
        Map<String, Card> defaultCards = new HashMap<>();
        //TODO these validations are wrong
        HashMap<String, Class<?>> cardRequiredInputs = new HashMap<>();
        cardRequiredInputs.put("discardCardTargetPlayer", String.class);
        cardRequiredInputs.put("modifyAttributeTargetPlayer", String.class);
        defaultCards.put("Card1", new Card("Card1", List.of(new DiscardCardEffect(), new ModifyAttributeEffect()), cardRequiredInputs));
        defaultCards.put("SuperDuper", new Card("SuperDuper", List.of(new ModifyAttributeEffect()), cardRequiredInputs));
        return defaultCards;
    }

    public GameState getGameState(String gameId) {
        return games.get(gameId);
    }

    private List<Card> generateDeck() {
        return new ArrayList<>(cards.values());
    }
}
