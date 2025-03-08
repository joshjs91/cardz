package com.joshjs.gamangine.factory;

import com.joshjs.gamangine.card.Card;
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
            Spanish41BaseEffect blueSpanishEffect = new Spanish41BaseEffect();
            blueSpanishEffect.setColour("blue");
            blueSpanishEffect.setNumber(number);
            Spanish41BaseEffect redeSpanishEffect = new Spanish41BaseEffect();
            redeSpanishEffect.setColour("red");
            redeSpanishEffect.setNumber(number);
            Spanish41BaseEffect greenSpanishEffect = new Spanish41BaseEffect();
            greenSpanishEffect.setColour("green");
            greenSpanishEffect.setNumber(number);
            Spanish41BaseEffect yellowSpanishEffect = new Spanish41BaseEffect();
            yellowSpanishEffect.setColour("yellow");
            yellowSpanishEffect.setNumber(number);
            deck.add(new Card(number + " blue", List.of(blueSpanishEffect)));
            deck.add(new Card(number + " red", List.of(redeSpanishEffect)));
            deck.add(new Card(number + " green", List.of(greenSpanishEffect)));
            deck.add(new Card(number + " yellow", List.of(yellowSpanishEffect)));
            Spanish41DrawCardEffect drawSpanishEffect = new Spanish41DrawCardEffect();
            drawSpanishEffect.setCardsToDraw(4);
            deck.add(new Card("Draw 4", List.of(drawSpanishEffect)));
        }

        return deck;
    }

    public static List<Card> generateSimpleSpanish41Deck() {
        List<Card> deck = new ArrayList<>();

        // Define base effects with correct numbers
        Spanish41BaseEffect blueSpanishEffect1 = new Spanish41BaseEffect();
        blueSpanishEffect1.setColour("blue");
        blueSpanishEffect1.setNumber(1);

        Spanish41BaseEffect redSpanishEffect1 = new Spanish41BaseEffect();
        redSpanishEffect1.setColour("red");
        redSpanishEffect1.setNumber(1);

        Spanish41BaseEffect greenSpanishEffect1 = new Spanish41BaseEffect();
        greenSpanishEffect1.setColour("green");
        greenSpanishEffect1.setNumber(1);

        Spanish41BaseEffect yellowSpanishEffect1 = new Spanish41BaseEffect();
        yellowSpanishEffect1.setColour("yellow");
        yellowSpanishEffect1.setNumber(1);

        // Define base effects with number 2
        Spanish41BaseEffect blueSpanishEffect2 = new Spanish41BaseEffect();
        blueSpanishEffect2.setColour("blue");
        blueSpanishEffect2.setNumber(2);

        Spanish41BaseEffect redSpanishEffect2 = new Spanish41BaseEffect();
        redSpanishEffect2.setColour("red");
        redSpanishEffect2.setNumber(2);

        Spanish41BaseEffect greenSpanishEffect2 = new Spanish41BaseEffect();
        greenSpanishEffect2.setColour("green");
        greenSpanishEffect2.setNumber(2);

        Spanish41BaseEffect yellowSpanishEffect2 = new Spanish41BaseEffect();
        yellowSpanishEffect2.setColour("yellow");
        yellowSpanishEffect2.setNumber(2);

        // Draw effect
        Spanish41DrawCardEffect drawSpanishEffect = new Spanish41DrawCardEffect();
        drawSpanishEffect.setCardsToDraw(2);

        // Add cards with appropriate effects
        deck.add(new Card("1 blue", List.of(blueSpanishEffect1)));
        deck.add(new Card("1 green", List.of(greenSpanishEffect1)));
        deck.add(new Card("2 yellow", List.of(yellowSpanishEffect2)));
        deck.add(new Card("Draw 2", List.of(drawSpanishEffect)));
        deck.add(new Card("2 red", List.of(redSpanishEffect2)));
        deck.add(new Card("Draw 2", List.of(drawSpanishEffect)));
        deck.add(new Card("2 red", List.of(redSpanishEffect2)));
        deck.add(new Card("2 green", List.of(greenSpanishEffect2)));
        deck.add(new Card("2 red", List.of(redSpanishEffect2)));
        deck.add(new Card("2 red", List.of(redSpanishEffect2)));
        deck.add(new Card("2 red", List.of(redSpanishEffect2)));
        deck.add(new Card("1 red", List.of(redSpanishEffect1)));
        deck.add(new Card("1 blue", List.of(blueSpanishEffect1)));
        deck.add(new Card("2 yellow", List.of(yellowSpanishEffect2)));
        deck.add(new Card("2 yellow", List.of(yellowSpanishEffect2)));
        deck.add(new Card("2 yellow", List.of(yellowSpanishEffect2)));
        deck.add(new Card("2 yellow", List.of(yellowSpanishEffect2)));
        deck.add(new Card("2 yellow", List.of(yellowSpanishEffect2)));

        return deck;
    }

}
