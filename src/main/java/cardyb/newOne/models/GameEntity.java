package cardyb.newOne.models;

import java.util.List;
import java.util.Map;

public class GameEntity {

    private final String id;

    //ordered list that determines the order of play
    private final List<PlayerEntity> players;

    private PlayerEntity currentPlayer;

    private final List<CardEntity> deck;

    private final List<CardEntity> discardPile;

    //attributes like pot, or random point things?
    private final Map<String, Object> attributes;

    public GameEntity(String id, List<PlayerEntity> players, PlayerEntity currentPlayer, List<CardEntity> deck, List<CardEntity> discardPile, Map<String, Object> attributes) {
        this.id = id;
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.deck = deck;
        this.discardPile = discardPile;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public List<PlayerEntity> getPlayers() {
        return players;
    }

    public PlayerEntity getCurrentPlayer() {
        return currentPlayer;
    }

    public List<CardEntity> getDeck() {
        return deck;
    }

    public List<CardEntity> getDiscardPile() {
        return discardPile;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }


}
