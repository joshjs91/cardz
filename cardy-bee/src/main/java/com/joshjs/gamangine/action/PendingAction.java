package com.joshjs.gamangine.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PendingAction {
    private String player;
    private PlayerAction action;
}
