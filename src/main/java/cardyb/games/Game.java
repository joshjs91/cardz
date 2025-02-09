package cardyb.games;

import cardyb.CardPile;
import cardyb.cards.Card;
import cardyb.cards.DiscardCard;
import cardyb.cards.DrawCard;
import cardyb.players.Player;

import java.util.*;

public class Game {
    private List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private Scanner scanner = new Scanner(System.in);
    private CardPile gameDeck = new CardPile("main deck");
    private CardPile gameDiscardPile = new CardPile("main discard pile");

    public Game(int numPlayers) {
        List<Card> tempDeck = new ArrayList<>();
        for (int i = 0; i < 50; i++) { // Increased number of cards
            tempDeck.add(new DrawCard("Draw 2", 2));
            tempDeck.add(new DiscardCard("Discard 2", 2));
        }
        gameDeck.addCards(tempDeck);

        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player("Player " + i, gameDeck, gameDiscardPile));
        }
    }

    public CardPile getGameDiscardPile() {
        return gameDiscardPile;
    }

    public CardPile getDeck() {
        return gameDeck;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void start() {
        System.out.println("cardyb.games.Game starts!");

        System.out.println("Draw initial cards!");
        for (Player player : players) {
            player.drawFromGameDeck(5, this);
        }

        while (players.stream().anyMatch(Player::hasCards)) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println(currentPlayer.getName() + "'s turn.");
            System.out.println("Your hand: " + currentPlayer.handToString());
            System.out.println("Your discard pile: " + currentPlayer.discardPileToString());
            System.out.println("cardyb.games.Game discard pile: " + gameDiscardPile.toString());
            if (currentPlayer.hasCards()) {
                System.out.print("Enter card index to play (or -1 to skip): ");
                int index = scanner.nextInt();
                if (index >= 0 && index < currentPlayer.getHand().size()) {
                    currentPlayer.playCard(index, this);
                } else {
                    System.out.println(currentPlayer.getName() + " skipped their turn.");
                }
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        System.out.println("cardyb.games.Game over!");
    }
}
