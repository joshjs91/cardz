package com.joshjs.gamangine.model.dto;

import com.joshjs.gamangine.action.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerActionRequest {
    public String gameId;
    public String playerId;
    public Action action;
    public Map<String, Object> actionData;
}
