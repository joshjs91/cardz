package cardyb.actions;

import cardyb.games.Game;
import cardyb.players.Player;

public class DiscardSelectedCardAction implements GameAction {

    private int cardsToDiscard = 2;

    public DiscardSelectedCardAction() {
    }

    public DiscardSelectedCardAction(int cardsToDiscard) {
        this.cardsToDiscard = cardsToDiscard;
    }

    @Override
    public void run(GameActionContext context) {
        Game game = context.getGame();
        Player player = context.getPlayer();
        for (int i = 0; i < cardsToDiscard; i++) {
            System.out.println("Your hand: " + player.handToString());
            if (player.hasCards()) {
                System.out.print("Enter card index to discard: ");
                int index = game.getScanner().nextInt();
                if (index >= 0 && index < player.getHand().size()) {
                    player.discardCardToGameDiscardPile(index, game);
                } else {
                    System.out.println("Invalid input. Try again.");
                    i--;
                }
            } else {
                System.out.println(player.getName() + " has no more cards to discard.");
            }
        }
    }
}
