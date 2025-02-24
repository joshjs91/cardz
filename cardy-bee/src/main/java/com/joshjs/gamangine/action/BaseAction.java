package com.joshjs.gamangine.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseAction implements Action {
    private Boolean isRequired = false;

    @Override
    public Boolean isRequired() {
        return isRequired;
    }
}
