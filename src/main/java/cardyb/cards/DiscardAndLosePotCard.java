package cardyb.cards;

import cardyb.actions.ChangePotAction;
import cardyb.actions.DiscardSelectedCardAction;

import java.util.List;

public class DiscardAndLosePotCard extends Card {
    public DiscardAndLosePotCard(String name, int amountToDiscard, Long potAmountToLose) {
        super(name);
        addActions(
            List.of(
                    new DiscardSelectedCardAction(amountToDiscard),
                    new ChangePotAction(potAmountToLose)
            )
        );
    }
}
