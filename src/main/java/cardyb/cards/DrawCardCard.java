package cardyb.cards;

import cardyb.actions.DiscardSelectedCardAction;
import cardyb.actions.DrawCardAction;

import java.util.List;

public class DrawCardCard extends Card {
    public DrawCardCard(String name, int cardsToDraw) {
        super(name);
        addActions(
                List.of(
                        new DrawCardAction(cardsToDraw)
                )
        );
    }
}
