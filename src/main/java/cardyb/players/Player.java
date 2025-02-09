package cardyb.players;

import java.util.ArrayList;
import cardyb.CardPile;
import cardyb.Colours;
import cardyb.cards.Card;
import cardyb.games.Game;

import java.util.List;

public class Player {
    private String name;
    private List<Card> hand = new ArrayList<>();
    private CardPile deck;
    private CardPile discardPile;

    public Player(String name, CardPile deck, CardPile discardPile) {
        this.name = name;
        this.deck = deck;
        this.discardPile = discardPile;
    }

    public String getName() {
        return name;
    }

    public CardPile getDeck() {
        return deck;
    }

    public CardPile getDiscardPile() {
        return discardPile;
    }

    public boolean hasCards() {
        return !hand.isEmpty();
    }

    public List<Card> getHand() {
        return hand;
    }

    public String discardPileToString() {
        return discardPile.toString();
    }

    public void drawFromGameDeck(int count, Game game) {
        drawCards(count, game.getDeck());
    }

    public void drawFromPersonalDeck(int count) {
        drawCards(count, getDeck());
    }

    public void drawCards(int count, CardPile from) {
        for (int i = 0; i < count; i++) {
            Card card = from.draw();
            if (card != null) {
                hand.add(card);
                System.out.println(name + " drew: " + card.getName() + " from " + from.getName());
            } else {
                System.out.println(name + " tried to draw but the" + from.getName() + " is empty.");
            }
        }
        System.out.println(name + " now has: " + handToString());
    }

    public void discardCardToGameDiscardPile(int index, Game game) {
        discardCard(index, game.getGameDiscardPile());
    }

    public void discardCard(int index, CardPile to) {
        if (index >= 0 && index < hand.size()) {
            Card card = hand.remove(index);
            System.out.println(name + " discards " + card.getName());
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