package cardyb.cards;

import cardyb.actions.DiscardSelectedCardAction;
import cardyb.games.Game;
import cardyb.players.Player;

import java.util.List;

public class DiscardCard extends Card {
    public DiscardCard(String name, int amountToDiscard) {
        super(name);
        addActions(
                List.of(
                        new DiscardSelectedCardAction(amountToDiscard)
                )
        );
    }
}
