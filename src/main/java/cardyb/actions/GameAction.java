package cardyb.actions;

import cardyb.games.Game;
import cardyb.players.Player;

@FunctionalInterface
public
interface GameAction {
    void run(Player player, Game game, Object... params); // Pass player and any number of parameters
}