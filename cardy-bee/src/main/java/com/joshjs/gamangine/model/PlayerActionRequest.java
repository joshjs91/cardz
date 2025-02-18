package com.joshjs.gamangine.model;

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
    public String actionType;
    public Map<String, Object> actionData;
}
