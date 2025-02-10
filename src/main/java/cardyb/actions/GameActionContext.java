package cardyb.actions;

import cardyb.games.Game;
import cardyb.players.Player;

import java.util.Optional;


/**
 * Context to pass into game actions
 */
public class GameActionContext {

    /**
     * Player who triggered the action. Optional in case it was not triggered by a user.
     */
    private final Optional<Player> player;

    /**
     * The game that the action is being triggered in. There will always be a game! I presume.
     */
    private final Game game;

    public GameActionContext(Player player, Game game) {
        this.player = Optional.ofNullable(player);
        this.game = game;
    }

    public Optional<Player> getPlayerOptional() {
        return player;
    }

    public Player getPlayer() {
        return player.orElseThrow(() -> new RuntimeException("WTF there is no player who triggered this action when there should be!"));
    }

    public Game getGame() {
        return game;
    }
}