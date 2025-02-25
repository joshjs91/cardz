package com.joshjs.gamangine.card;

import com.joshjs.gamangine.card.effects.DiscardCardEffect;
import com.joshjs.gamangine.card.effects.ModifyGameNumberAttributeEffect;

import java.util.*;

public class DeckFactory {

    public static List<Card> generateDeck(String deckType) {
        return switch (deckType) {
            case "default" -> generateDefaultDeck();
            case "complex" -> generateComplexDeck();
            case "tokenCards" -> generateDefaultDeck().stream()
                    .filter(card -> card.getName().contains("tokens"))
                    .toList();
            case "noEffectsCards" -> generateDefaultDeck().stream()
                    .filter(card -> card.getName().contains("No effects"))
                    .toList();
            default -> generateDefaultDeck();
        };
    }

    private static List<Card> generateDefaultDeck() {
        List<Card> deck = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            deck.add(new Card("Remove tokens", List.of(new ModifyGameNumberAttributeEffect())));
            deck.add(new Card("Card1", List.of()));
            deck.add(new Card("SuperDuper", List.of()));
            deck.add(new Card("SuperDuper pooper", List.of()));
            deck.add(new Card("No effects", List.of()));
        }

        return deck;
    }

    private static List<Card> generateComplexDeck() {
        List<Card> deck = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            deck.add(new Card("Card1", List.of(new DiscardCardEffect(), new ModifyGameNumberAttributeEffect())));
            deck.add(new Card("SuperDuper", List.of(new ModifyGameNumberAttributeEffect())));
        }

        return deck.subList(0, 20); // Limit deck to 20 cards
    }
}
