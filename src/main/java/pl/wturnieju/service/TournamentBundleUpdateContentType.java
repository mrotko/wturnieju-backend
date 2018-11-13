package pl.wturnieju.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TournamentBundleUpdateContentType {

    @JsonProperty("TOURNAMENT_UPDATE_TYPE.START")
    START,

    @JsonProperty("TOURNAMENT_UPDATE_TYPE.NEXT_ROUND")
    NEXT_ROUND,

    @JsonProperty("TOURNAMENT_UPDATE_TYPE.PAUSE")
    PAUSE,

    @JsonProperty("TOURNAMENT_UPDATE_TYPE.END")
    END
}
