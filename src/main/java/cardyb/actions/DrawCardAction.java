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
    public void run(GameActionContext context) {
        Game game = context.getGame();
        Player player = context.getPlayer();
        player.drawCards(cardsToDraw, game.getDeck());
    }
}
