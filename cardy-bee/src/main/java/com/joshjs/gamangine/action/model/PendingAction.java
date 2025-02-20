package com.joshjs.gamangine.action.model;

import com.joshjs.gamangine.action.Action;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PendingAction {
    private String player;
    private Action action;
}
