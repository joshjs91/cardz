package cardyb.newOne.models;

import java.util.List;
import java.util.Map;

public class PlayerEntity {
    // some uuid
    private String id;

    private List<ActionEntity> defaultTurnActions;

    private List<ActionEntity> nowActions;

    private List<List<ActionEntity>> nextActions;

    //stack or card names
    private List<CardEntity> discardPile;

    //stack or card names
    private List<CardEntity> deck;

    //stack or card names
    private Map<String, Object> attributes;

    public PlayerEntity(String id, List<ActionEntity> defaultTurnActions, List<ActionEntity> nowActions, List<List<ActionEntity>> nextActions, List<CardEntity> discardPile, List<CardEntity> deck, Map<String, Object> attributes) {
        this.id = id;
        this.defaultTurnActions = defaultTurnActions;
        this.nowActions = nowActions;
        this.nextActions = nextActions;
        this.discardPile = discardPile;
        this.deck = deck;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public List<ActionEntity> getDefaultTurnActions() {
        return defaultTurnActions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDefaultTurnActions(List<ActionEntity> defaultTurnActions) {
        this.defaultTurnActions = defaultTurnActions;
    }

    public void setNowActions(List<ActionEntity> nowActions) {
        this.nowActions = nowActions;
    }

    public void setNextActions(List<List<ActionEntity>> nextActions) {
        this.nextActions = nextActions;
    }

    public void setDiscardPile(List<CardEntity> discardPile) {
        this.discardPile = discardPile;
    }

    public void setDeck(List<CardEntity> deck) {
        this.deck = deck;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<ActionEntity> getNowActions() {
        return nowActions;
    }

    public List<List<ActionEntity>> getNextActions() {
        return nextActions;
    }

    public List<CardEntity> getDiscardPile() {
        return discardPile;
    }

    public List<CardEntity> getDeck() {
        return deck;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
