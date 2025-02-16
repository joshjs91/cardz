package cardyb.newOne;

import cardyb.newOne.game.Game;
import cardyb.newOne.models.ActionEntity;
import cardyb.newOne.models.GameEntity;
import cardyb.newOne.player.Player;
import cardyb.newOne.repository.GameRepository;
import cardyb.newOne.models.PlayerEntity;
import cardyb.newOne.service.GameService;

import java.util.Objects;
import java.util.Optional;

//does the validation of input and runs the action on the game
public class ActionProcessor {

    private final GameService gameService;

    public ActionProcessor(GameService gameService) {
        this.gameService = gameService;
    }

    public void processAction(String gameId, String playerId, String actionId) {
        Game game = gameService.getGame(gameId);

        Player player = getValidPlayer(game, playerId);
        ActionEntity action = getValidAction(actionId, player);
    }


    private ActionEntity getValidAction(String actionId, Player playerMakingAction) {
        Optional<ActionEntity> availableAction = playerMakingAction.getNowActions().stream().filter(action -> action.getName().equals(actionId)).findFirst();
        if (availableAction.isEmpty()) {
            throw new RuntimeException("Action not valid for user!");
        }

        return availableAction.get();
    }

    //TODO test this method out.....
    private Player getValidPlayer(Game game, String playerId) {
        Optional<Player> playerMakingActionOpt = game.getPlayers().stream().filter(player -> Objects.equals(playerId, player.getId())).findFirst();
        if (playerMakingActionOpt.isEmpty()) {
            throw new RuntimeException("Not a valid player for provided game!");
        }
        return playerMakingActionOpt.get();
    }
}
