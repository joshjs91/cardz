package com.joshjs.gamangine.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joshjs.gamangine.condition.Condition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameSetupRequest {

    @NotEmpty(message = "playerIds must not be null")
    private List<String> playerIds;

    @NotNull(message = "gameEndedCondition must not be null")
    private Condition gameEndedCondition;

    private Map<String, Object> gameAttributes;

    @NotBlank(message = "deckType must not be empty")  // Use @NotBlank instead of @NotNull
    private String deckType;

    private Map<String, Map<String, Object>> playerAttributes;
}
