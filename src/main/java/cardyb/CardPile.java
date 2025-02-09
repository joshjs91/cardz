package cardyb;


import cardyb.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

// TODO add shuffle
public class CardPile {

    public CardPile(String name) {
        this.name = name;
    }

    private String name;

    private Stack<Card> cards = new Stack<>();

    public void addCard(Card card) {
        cards.push(card);
    }

    public void addCards(List<Card> newCards) {
        Collections.shuffle(newCards);
        cards.addAll(newCards);
    }

    public void shufflePile() {
        List<Card> cardList = new ArrayList<>(cards);

        Collections.shuffle(cardList);
        cards.clear();
        cards.addAll(cardList);
    }

    public Card draw() {
        return cards.isEmpty() ? null : cards.pop();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public String toString() {
        List<String> cardNames = new ArrayList<>();
        for (Card c : cards) {
            cardNames.add(c.getName());
        }
        return cardNames.isEmpty() ? "Empty" : String.join(", ", cardNames);
    }

    public String getName() {
        return name;
    }
}
