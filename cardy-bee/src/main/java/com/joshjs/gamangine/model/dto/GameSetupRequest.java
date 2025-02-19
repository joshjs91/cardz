package com.joshjs.gamangine.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joshjs.gamangine.action.handlers.ActionHandler;
import com.joshjs.gamangine.condition.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSetupRequest {
    private List<String> playerIds;
    private Condition gameEndingCondition;

}
