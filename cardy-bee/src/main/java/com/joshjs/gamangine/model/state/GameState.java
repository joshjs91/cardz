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
    //TODO maybe i do need a player class
    private List<String> players;
    private Map<String, Map<String, Object>> playerAttributes;
    private String currentPlayer;
    private Map<String, Object> gameAttributes;
    private Map<String, List<Action>> playerAvailableActions= new HashMap<>();;
    private Queue<PendingAction> pendingActions;

    private List<Card> drawDeck;
    private List<Card> discardPile;
    private Map<String, List<Card>> playerHands;

    private Condition gameEndedCondition;

    public boolean isGameEnded() {
        return gameEndedCondition != null && gameEndedCondition.evaluate(this);
    }
}