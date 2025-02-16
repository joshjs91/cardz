package cardyb.newOne;

import cardyb.newOne.models.CardEntity;

import java.util.ArrayList;
import java.util.List;

public class DeckFactory {

    public static List<CardEntity> createStandardDeck() {
        List<CardEntity> deck = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            //these will be stored as types Enums match to a class of card that has all of the bits in code
            deck.add(new CardEntity("SOME_SPELL"));
            deck.add(new CardEntity("SUPER_SPELL"));
            deck.add(new CardEntity("SHIELD"));
            deck.add(new CardEntity("BANK_ROBBERY"));
            deck.add(new CardEntity("REDRAW"));
        }
        return deck;
    }

}
