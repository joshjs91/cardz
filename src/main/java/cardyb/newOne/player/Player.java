package cardyb.newOne.player;

import cardyb.CardPile;
import cardyb.Colours;
import cardyb.cards.Card;
import cardyb.games.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {
    private String id;
    private Map<String, Object> attributes;
    private List<Card> hand = new ArrayList<>();
    private CardPile deck;
    private CardPile discardPile;

    public Player(String id, CardPile deck, CardPile discardPile) {
        this.id = id;
        this.deck = deck;
        this.discardPile = discardPile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public CardPile getDeck() {
        return deck;
    }

    public void setDeck(CardPile deck) {
        this.deck = deck;
    }

    public CardPile getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(CardPile discardPile) {
        this.discardPile = discardPile;
    }

    public void changeAttribute(String attribute, Object newValue) {
        //TODO check if attribute is valid
        System.out.println(id + " has " + attribute + " changed from " + newValue.toString());
        this.attributes.put(attribute, newValue);
    }

    public void drawCards(int count, CardPile from) {
        for (int i = 0; i < count; i++) {
            Card card = from.draw();
            if (card != null) {
                hand.add(card);
                System.out.println(id + " drew: " + card.getName() + " from " + from.getName());
            } else {
                System.out.println(id + " tried to draw but the" + from.getName() + " is empty.");
            }
        }
        System.out.println(id + " now has: " + handToString());
    }

    public void discardCard(int index, CardPile to) {
        if (index >= 0 && index < hand.size()) {
            Card card = hand.remove(index);
            System.out.println(id + " discards " + card.getName());
            to.addCard(card);
        } else {
            System.out.println("Invalid card index!");
        }
    }

    public void playCard(int index, Game game) {
        if (index >= 0 && index < hand.size()) {
            Card card = hand.remove(index);
            System.out.println(name + " plays " + card.getName());
            card.play(this, game);
            discardPile.addCard(card);
        } else {
            System.out.println("Invalid card index!");
        }
    }

    public String handToString() {
        List<String> cardNames = new ArrayList<>();
        for (Card c : hand) {
            cardNames.add(c.getName());
        }
        String cards = String.join(", ", cardNames);
        return Colours.YELLOW + cards + Colours.RESET;
    }

}