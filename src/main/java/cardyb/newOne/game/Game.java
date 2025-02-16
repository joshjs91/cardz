package cardyb.newOne.game;

import cardyb.CardPile;
import cardyb.newOne.models.ActionEntity;
import cardyb.newOne.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private final List<Player> players;
    private final Player currentPlayer;
    private final CardPile gameDeck;
    private final CardPile gameDiscardPile;

    public Game(List<Player> players, Player currentPlayer, CardPile gameDeck, CardPile gameDiscardPile) {
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.gameDeck = gameDeck;
        this.gameDiscardPile = gameDiscardPile;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public CardPile getGameDeck() {
        return gameDeck;
    }

    public CardPile getGameDiscardPile() {
        return gameDiscardPile;
    }

    public void runAction(Player player, Action)
}
