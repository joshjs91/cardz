package cardyb.cards;

import cardyb.actions.GameAction;
import cardyb.actions.GameActionContext;
import cardyb.games.Game;
import cardyb.players.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Card {
    private final String name;
    private final List<GameAction> actions = new ArrayList<>();

    public Card(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addActions(List<GameAction> actions) {
        this.actions.addAll(actions);
    }

    public void play(Player player, Game game, Object... params) {
        System.out.println("\n--- " + player + " is playing " + name + " ---");
        for (GameAction action : actions) {
            action.run(new GameActionContext(player, game));  // Execute the action (either default or overridden)
        }
    }
}

