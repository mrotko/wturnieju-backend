package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FixtureStatus {

    @JsonProperty("FIXTURE_STATUS.BEFORE_START")
    BEFORE_START,

    @JsonProperty("FIXTURE_STATUS.IN_PROGRESS")
    IN_PROGRESS,

    @JsonProperty("FIXTURE_STATUS.CANCELED")
    CANCELED,

    @JsonProperty("FIXTURE_STATUS.DELAYED")
    DELAYED,

    @JsonProperty("FIXTURE_STATUS.FIRST_PLAYER_WON")
    FIRST_PLAYER_WON,

    @JsonProperty("FIXTURE_STATUS.FIRST_PLAYER_WON_WALKOVER")
    FIRST_PLAYER_WON_WALKOVER,

    @JsonProperty("FIXTURE_STATUS.SECOND_PLAYER_WON")
    SECOND_PLAYER_WON,

    @JsonProperty("FIXTURE_STATUS.SECOND_PLAYER_WON_WALKOVER")
    SECOND_PLAYER_WON_WALKOVER,

    @JsonProperty("FIXTURE_STATUS.DRAW")
    DRAW
}
