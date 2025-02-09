package cardyb.cards;

import cardyb.games.Game;
import cardyb.players.Player;

public class DiscardCard extends Card {
    private int cardsToDiscard;

    public DiscardCard(String name, int discardCount) {
        super(name);
        this.cardsToDiscard = discardCount;
    }

    @Override
    public void play(Player player, Game game) {
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
        game.getGameDiscardPile().addCard(this);
    }
}
