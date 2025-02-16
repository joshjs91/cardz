package cardyb.newOne.service;


import cardyb.newOne.game.Game;
import cardyb.newOne.models.GameEntity;
import cardyb.newOne.models.PlayerEntity;
import cardyb.newOne.player.Player;
import cardyb.newOne.repository.GameRepository;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game getGame(String gameId) {
        GameEntity game = gameRepository.getGame(gameId).orElseThrow(() -> new RuntimeException("No game of this id"));
        List<Player> players = new ArrayList<>();

        for (PlayerEntity player : game.getPlayers()) {
            players.add(new Player(player.getId(), null, null));
        }
        Player currentPlayer = getCurrentPlayer(game, players);


        return new Game(players, currentPlayer, null, null)
    }

    private Player getCurrentPlayer(GameEntity game, List<Player> players) {
        return players.stream().filter(player -> player.getId().equals(game.getCurrentPlayer().getId())).findFirst().orElseThrow(() -> new RuntimeException("Couldn't find the current player"));
    }





}
