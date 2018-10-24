package pl.wturnieju.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum TournamentSystemType {

    @JsonProperty("TOURNAMENT_SYSTEM_TYPE.SWISS")
    SWISS,
    @JsonProperty("TOURNAMENT_SYSTEM_TYPE.KNOCKOUT")
    KNOCKOUT,
    @JsonProperty("TOURNAMENT_SYSTEM_TYPE.GROUP")
    GROUP,
    @JsonProperty("TOURNAMENT_SYSTEM_TYPE.LEAGUE")
    LEAGUE,
    @JsonProperty("TOURNAMENT_SYSTEM_TYPE.ROUND_ROBIN")
    ROUND_ROBIN,
    @JsonProperty("TOURNAMENT_SYSTEM_TYPE.DOUBLE_ELIMINATION")
    DOUBLE_ELIMINATION
}
