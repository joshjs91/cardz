package com.joshjs.gamangine.controller;

import com.joshjs.gamangine.condition.CardsAllPlayedCondition;
import com.joshjs.gamangine.condition.Condition;
import com.joshjs.gamangine.condition.GameNumberAttributeCondition;
import com.joshjs.gamangine.model.dto.GameSetupRequest;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public GameState startGame(@RequestBody GameSetupRequest request) {
        return gameService.startGame(request);
    }

    @GetMapping("/conditions")
    public List<Condition> performAction() {
        return List.of(new CardsAllPlayedCondition(), new GameNumberAttributeCondition());
    }

    @PostMapping("/action")
    public GameState performAction(@RequestBody PlayerActionRequest action) {
        return gameService.executeAction(action);
    }

    @GetMapping("/state/{gameId}")
    public GameState getGameState(@PathVariable String gameId) {
        return gameService.getGameState(gameId);
    }

}

