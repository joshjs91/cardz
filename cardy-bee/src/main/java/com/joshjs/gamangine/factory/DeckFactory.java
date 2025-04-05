package com.joshjs.gamangine.factory;

import com.joshjs.gamangine.card.Card;
import com.joshjs.gamangine.card.ColourCard;
import com.joshjs.gamangine.card.NumberAndColourCard;
import com.joshjs.gamangine.card.effects.DiscardCardEffect;
import com.joshjs.gamangine.card.effects.ModifyGameNumberAttributeEffect;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41BaseEffect;
import com.joshjs.gamangine.card.effects.spanish41.Spanish41DrawCardEffect;

import java.util.*;

public class DeckFactory {

    public static List<Card> generateDeck(String deckType) {
        return switch (deckType) {
            case "default" -> generateDefaultDeck();
            case "complex" -> generateComplexDeck();
            case "spanish41" -> generateSpanish41Deck();
            case "simpleSpanish41" -> generateSimpleSpanish41Deck();
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
            ModifyGameNumberAttributeEffect removeTokensEffect = new ModifyGameNumberAttributeEffect();
            removeTokensEffect.setAttribute("tokens");
            removeTokensEffect.setCalculationType("minus");
            deck.add(new Card("Remove tokens", List.of(removeTokensEffect)));
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

        return deck;
    }

    private static List<Card> generateSpanish41Deck() {
        List<Card> deck = new ArrayList<>();
        for (int number = 0; number < 10; number++) {
            deck.add(new NumberAndColourCard("some spanish41 card", " blue", number, List.of(new Spanish41BaseEffect())));
            deck.add(new NumberAndColourCard("some spanish41 card", " red", number, List.of(new Spanish41BaseEffect())));
            deck.add(new NumberAndColourCard("some spanish41 card", " green", number, List.of(new Spanish41BaseEffect())));
            deck.add(new NumberAndColourCard("some spanish41 card", " yellow",number, List.of(new Spanish41BaseEffect())));
            Spanish41DrawCardEffect drawSpanishEffect = new Spanish41DrawCardEffect();
            drawSpanishEffect.setCardsToDraw(4);
            deck.add(new Card("Draw 4", List.of(drawSpanishEffect)));
        }

        return deck;
    }

    public static List<Card> generateSimpleSpanish41Deck() {
        List<Card> deck = new ArrayList<>();

        // Define base effects with correct numbers
        Card blue1 = new NumberAndColourCard("1 blue", "blue", 1, List.of(new Spanish41BaseEffect()));
        Card red1 = new NumberAndColourCard("1 red", "red", 1, List.of(new Spanish41BaseEffect()));
        Card green1 = new NumberAndColourCard("1 green", "green", 1, List.of(new Spanish41BaseEffect()));

        Card red2 = new NumberAndColourCard("2 red", "red", 2, List.of(new Spanish41BaseEffect()));
        Card green2 = new NumberAndColourCard("2 green", "green", 2, List.of(new Spanish41BaseEffect()));
        Card yellow2 = new NumberAndColourCard("2 yellow", "yellow", 2, List.of(new Spanish41BaseEffect()));

        // Draw effect
        Spanish41DrawCardEffect drawSpanishEffect = new Spanish41DrawCardEffect();
        drawSpanishEffect.setCardsToDraw(2);

        // Add cards with appropriate effects
        deck.add(blue1);
        deck.add(green1);
        deck.add(yellow2);
        deck.add(new ColourCard("Draw 2", "green", List.of(drawSpanishEffect)));
        deck.add(red2);
        deck.add(new ColourCard("Draw 2", "green", List.of(drawSpanishEffect)));
        deck.add(red2);
        deck.add(green2);
        deck.add(red2);
        deck.add(red2);
        deck.add(red2);
        deck.add(red1);
        deck.add(blue1);
        deck.add(yellow2);
        deck.add(yellow2);
        deck.add(yellow2);
        deck.add(yellow2);
        deck.add(yellow2);

        return deck;
    }

}
