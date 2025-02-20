package com.joshjs.gamangine.model.state;

import com.joshjs.gamangine.action.PendingAction;
import com.joshjs.gamangine.action.PlayerAction;
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
    private String currentPlayer;
    private Map<String, Object> gameAttributes;
    private Map<String, List<PlayerAction>> playerAvailableActions;
    private Queue<PendingAction> pendingActions;

    private List<Card> drawDeck;
    private List<Card> discardPile;
    private Map<String, List<Card>> playerHands;

    private Condition gameEndedCondition;

    public boolean isGameEnded() {
        return gameEndedCondition != null && gameEndedCondition.evaluate(this);
    }
}