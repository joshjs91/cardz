package cardyb.actions;

import cardyb.games.Game;
import cardyb.players.Player;

public class ChangePotAction implements GameAction {

    private Long potAmountToLose = 500L;

    public ChangePotAction() {
    }

    public ChangePotAction(Long potAmountToLose) {
        this.potAmountToLose = potAmountToLose;
    }

    @Override
    public void run(Player player, Game game, Object... params) {
        player.changePotBy(potAmountToLose);
    }
}
