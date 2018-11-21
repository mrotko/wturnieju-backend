package pl.wturnieju.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TournamentBundleUpdateContentType {

    @JsonProperty("TOURNAMENT_BUNDLE_UPDATE_CONTENT_TYPE.START")
    START,

    @JsonProperty("TOURNAMENT_BUNDLE_UPDATE_CONTENT_TYPE.NEXT_ROUND")
    NEXT_ROUND,

    @JsonProperty("TOURNAMENT_BUNDLE_UPDATE_CONTENT_TYPE.PAUSE")
    PAUSE,

    @JsonProperty("TOURNAMENT_BUNDLE_UPDATE_CONTENT_TYPE.END")
    END
}
