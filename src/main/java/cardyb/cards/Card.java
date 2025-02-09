package cardyb.cards;

import cardyb.games.Game;
import cardyb.players.Player;

public abstract class Card {
    protected String name;

    public Card(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void play(Player player, Game game);
}

