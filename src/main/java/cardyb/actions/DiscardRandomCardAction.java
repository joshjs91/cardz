package cardyb.actions;

import cardyb.games.Game;
import cardyb.players.Player;

import java.util.Random;

public class DiscardRandomCardAction implements GameAction {

    private int cardsToDiscard = 2;

    public DiscardRandomCardAction() {
    }

    public DiscardRandomCardAction(int cardsToDiscard) {
        this.cardsToDiscard = cardsToDiscard;
    }

    @Override
    public void run(GameActionContext context) {
        Game game = context.getGame();
        Player player = context.getPlayer();
        for (int i = 0; i < cardsToDiscard; i++) {
            System.out.println("Your hand: " + player.handToString());
            if (player.hasCards()) {
                int index = getRandomIndexFromHand(player);
                if (index < player.getHand().size()) {
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

    private int getRandomIndexFromHand(Player player) {
        Random random = new Random();
        int index = random.nextInt((player.getHand().size()));
        return index;
    }
}
