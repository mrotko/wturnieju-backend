package pl.wturnieju.tournament;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TournamentStatus {

    @JsonProperty("TOURNAMENT_STATUS.BEFORE_START")
    BEFORE_START,

    @JsonProperty("TOURNAMENT_STATUS.IN_PROGRESS")
    IN_PROGRESS,

    @JsonProperty("TOURNAMENT_STATUS.ENDED")
    ENDED
}
