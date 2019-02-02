package pl.wturnieju.controller.dto.game.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GameEventType {

    @JsonProperty("GAME_EVENT_TYPE.GAME_START")
    GAME_START,

    @JsonProperty("GAME_EVENT_TYPE.GAME_FINISHED")
    GAME_FINISHED,

    @JsonProperty("GAME_EVENT_TYPE.PARTICIPANT_EVENT")
    PARTICIPANT_EVENT
}
