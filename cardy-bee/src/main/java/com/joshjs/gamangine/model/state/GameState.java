package com.joshjs.gamangine.model.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joshjs.gamangine.action.model.PendingAction;
import com.joshjs.gamangine.action.Action;
import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.condition.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
//TODO make the list attributes final so they cant be tampered with outside of game state.
public class GameState {
    private String gameId;
    private List<String> players;
    private Map<String, Map<String, Object>> playerAttributes;
    private String currentPlayer;
    private Map<String, Object> gameAttributes;
    // Actions currently available to a user, they may be required or not, and also only actionable given there are no pending actions for other users
    private Map<String, List<Action>> playerAvailableActions;
    // Actions required to be done in sequence
    private Queue<PendingAction> pendingActions;

    private List<Card> drawDeck;
    private List<Card> discardPile;
    private Map<String, List<Card>> playerHands;

    private Condition gameEndedCondition;

    public boolean isGameEnded() {
        return gameEndedCondition != null && gameEndedCondition.evaluate(this);
    }

    //TODO add tests for all these
    @JsonIgnore
    public void changeTurns() {
        int currentIndex = players.indexOf(getCurrentPlayer());
        setCurrentPlayer(getPlayers().get((currentIndex + 1) % getPlayers().size()));
        System.out.println("Turn changed!!!!!!!&&&**&*&*&*&* to player: " + getCurrentPlayer());
    }

    @JsonIgnore
    public void drawCardsFromGameDeck(String player, int count) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < count && !drawDeck.isEmpty(); i++) {
            drawnCards.add(drawDeck.remove(0));
        }
        List<Card> playersHand = getPlayerHands().get(player);
        if (playersHand == null) {
            getPlayerHands().put(player, drawnCards);
        } else {
            playersHand.addAll(drawnCards);
        }
        System.out.println("Player " + player + " drew " + count + " cards!");
    }

    @JsonIgnore
    public void addActionsToPlayer(String player, List<Action> actions) {
        if (playerAvailableActions.get(player) != null) {
            playerAvailableActions.get(player).addAll(actions);
        } else {
            List<Action> userActions = new ArrayList<>(actions);
            playerAvailableActions.put(player, userActions);
        }
    }

    @JsonIgnore
    public void removePlayersActions(String player) {
        if (playerAvailableActions.get(player) != null) {
            playerAvailableActions.get(player).clear();
        } else {
            playerAvailableActions.put(player, new ArrayList<>());
        }
    }

    @JsonIgnore
    public void addPendingActions(PendingAction pendingAction) {
        if (pendingActions == null) {
            pendingActions = new LinkedList<>();
        }
        pendingActions.add(pendingAction);
    }
}