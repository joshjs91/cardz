package cardyb.actions;

import cardyb.players.Player;

public class ChangePotAction implements GameAction {

    private Long potAmountToLose = 500L;

    public ChangePotAction() {
    }

    public ChangePotAction(Long potAmountToLose) {
        this.potAmountToLose = potAmountToLose;
    }

    @Override
    public void run(GameActionContext context) {
        Player player = context.getPlayer();
        player.changeAttribute(potAmountToLose);
    }
}
