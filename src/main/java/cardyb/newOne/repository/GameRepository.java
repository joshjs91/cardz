package cardyb.newOne.repository;

import cardyb.newOne.models.GameEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameRepository {

    private Map<String, GameEntity> games = new HashMap<>();

    public void putGame(GameEntity game) {
        games.put(game.getId(), game);
    }

    public Optional<GameEntity> getGame(String id) {
        GameEntity gameEntity = games.get(id);
        return gameEntity != null ? Optional.of(gameEntity) : Optional.empty();
    }
}
