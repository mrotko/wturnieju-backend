package pl.wturnieju.model;


import com.fasterxml.jackson.annotation.JsonProperty;

// TODO(mr): 02.02.2019 remove custom
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

    // TODO(mr): 02.02.2019 remove custom
    @JsonProperty("TOURNAMENT_SYSTEM_TYPE.CUSTOM")
    CUSTOM,
}
