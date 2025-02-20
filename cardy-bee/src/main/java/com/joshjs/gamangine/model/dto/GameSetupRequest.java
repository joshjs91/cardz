package com.joshjs.gamangine.model.dto;

import com.joshjs.gamangine.condition.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSetupRequest {
    private List<String> playerIds;
    private Condition gameEndedCondition;
    private Map<String, Object> gameAttributes;
    private String deckType;

}
