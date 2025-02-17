package com.joshjs.gamangine.controller;

import com.joshjs.gamangine.action.PlayerAction;
import com.joshjs.gamangine.model.GameSetupRequest;
import com.joshjs.gamangine.model.GameState;
import com.joshjs.gamangine.service.GameService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/action")
    public GameState performAction(@RequestBody PlayerAction action) {
        return gameService.executeAction(action);
    }

    @GetMapping("/state/{gameId}")
    public GameState getGameState(@PathVariable String gameId) {
        return gameService.getGameState(gameId);
    }

    @GetMapping("/")
    public String home() {
        return "Greetings!";
    }
}

