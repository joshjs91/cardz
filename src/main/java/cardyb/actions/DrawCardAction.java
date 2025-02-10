package cardyb.actions;

import cardyb.games.Game;
import cardyb.players.Player;

public class DrawCardAction implements GameAction {

    private int cardsToDraw = 2;

    public DrawCardAction() {
    }

    public DrawCardAction(int cardsToDraw) {
        this.cardsToDraw = cardsToDraw;
    }

    @Override
    public void run(Player player, Game game, Object... params) {
        player.drawCards(cardsToDraw, game.getDeck());
    }
}
