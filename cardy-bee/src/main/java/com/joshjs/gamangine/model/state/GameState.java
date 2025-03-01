package com.joshjs.gamangine.model.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class GameState {
    private String gameId;
    private List<String> players;
    private Map<String, Map<String, Object>> playerAttributes;
    private String currentPlayer;
    private Map<String, Object> gameAttributes;
    // Actions currently available to a user, they may be required or not, and also only actionable given there are no pending actions for other users
    private Map<String, List<Action>> playerAvailableActions= new HashMap<>();
    // Actions required to be done in sequence
    private Queue<PendingAction> pendingActions = new LinkedList<>();

    private List<Card> drawDeck;
    private List<Card> discardPile;
    private Map<String, List<Card>> playerHands;

    private Condition gameEndedCondition;

    public boolean isGameEnded() {
        return gameEndedCondition != null && gameEndedCondition.evaluate(this);
    }
}