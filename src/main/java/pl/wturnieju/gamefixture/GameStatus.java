package pl.wturnieju.gamefixture;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GameStatus {

    @JsonProperty("GAME_STATUS.BEFORE_START")
    BEFORE_START,

    @JsonProperty("GAME_STATUS.IN_PROGRESS")
    IN_PROGRESS,

    @JsonProperty("GAME_STATUS.ENDED")
    ENDED
}
