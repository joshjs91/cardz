package com.joshjs.gamangine.model;

import com.joshjs.gamangine.action.PlayerAction;
import com.joshjs.gamangine.card.Card;

import java.util.*;

public class GameState {
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
