package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DuelResult {

    @JsonProperty("DUEL_RESULT.HOME_PLAYER_WON")
    HOME_PLAYER_WON,

    @JsonProperty("DUEL_RESULT.HOME_PLAYER_WON_WALKOVER")
    HOME_PLAYER_WON_WALKOVER,

    @JsonProperty("DUEL_RESULT.AWAY_PLAYER_WON")
    AWAY_PLAYER_WON,

    @JsonProperty("DUEL_RESULT.AWAY_PLAYER_WON_WALKOVER")
    AWAY_PLAYER_WON_WALKOVER,

    @JsonProperty("DUEL_RESULT.DRAW")
    DRAW
}
