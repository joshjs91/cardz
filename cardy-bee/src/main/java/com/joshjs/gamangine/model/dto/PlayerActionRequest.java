package com.joshjs.gamangine.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joshjs.gamangine.action.Action;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerActionRequest {

//    @NotBlank(message = "gameId must not be null")
    public String gameId;

//    @NotBlank(message = "playerId must not be null")
    public String playerId;

//    @NotNull(message = "action must not be null")
    public Action action;
}
