package com.joshjs.gamangine.controller;

import com.joshjs.gamangine.condition.AllPlayersHaveNoCardsInHandCondition;
import com.joshjs.gamangine.condition.Condition;
import com.joshjs.gamangine.condition.GameNumberAttributeCondition;
import com.joshjs.gamangine.model.dto.GameSetupRequest;
import com.joshjs.gamangine.model.dto.PlayerActionRequest;
import com.joshjs.gamangine.model.state.GameState;
import com.joshjs.gamangine.service.GameService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
@Validated
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public GameState startGame(@Valid @RequestBody GameSetupRequest request) {
        System.out.println("Received request: "+ request);
        return gameService.startGame(request);
    }

    @GetMapping("/conditions")
    public List<Condition> performAction() {
        return List.of(new AllPlayersHaveNoCardsInHandCondition(), new GameNumberAttributeCondition());
    }

    @PostMapping("/action")
    public GameState performAction(@Valid @RequestBody PlayerActionRequest action) {
        return gameService.executeAction(action);
    }

    @GetMapping("/state/{gameId}")
    public GameState getGameState(@PathVariable String gameId) {
        return gameService.getGameState(gameId);
    }

}

