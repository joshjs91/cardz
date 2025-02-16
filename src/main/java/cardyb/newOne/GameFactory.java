package cardyb.newOne;

import cardyb.newOne.models.CardEntity;
import cardyb.newOne.models.GameEntity;
import cardyb.newOne.models.PlayerEntity;

import java.util.*;

public class GameFactory {
    public static GameEntity createGame(Integer numberOfPlayers) {
        String id = UUID.randomUUID().toString();

        List<PlayerEntity> players = createPlayers(numberOfPlayers);
        PlayerEntity currentPlayer = players.get(0);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("pot", 0);
        attributes.put("tokens", 5);

        List<CardEntity> standardDeck = DeckFactory.createStandardDeck();
        ArrayList<CardEntity> discardPile = new ArrayList<>();

        return new GameEntity(id, players, currentPlayer, standardDeck, discardPile, attributes);
    }

    private static List<PlayerEntity> createPlayers(Integer numberOfPlayers) {
        List<PlayerEntity> players = new ArrayList<>();
        for (int i = 1; i < numberOfPlayers; i++) {
            players.add(PlayerFactory.createStandardPlayer());
        }
        players.add(PlayerFactory.createPlayerWithNowActions());
        return players;
    }

}
