package cardyb.cards;

import cardyb.games.Game;
import cardyb.players.Player;

public class DrawCard extends Card {
    private int cardsToDraw;

    public DrawCard(String name, int cardsToDraw) {
        super(name);
        this.cardsToDraw = cardsToDraw;
    }

    @Override
    public void play(Player player, Game game) {
        player.drawFromGameDeck(cardsToDraw, game);
        game.getGameDiscardPile().addCard(this);
    }
}
