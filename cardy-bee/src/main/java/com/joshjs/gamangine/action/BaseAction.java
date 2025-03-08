package com.joshjs.gamangine.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseAction implements Action {

    @JsonIgnore
    private Boolean required = false;

    @Override
    public Boolean isRequired() {
        return required;
    }
}
