package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CompetitionType {
    @JsonProperty("COMPETITION_TYPE.CHESS")
    CHESS,

    @JsonProperty("COMPETITION_TYPE.FOOTBALL")
    FOOTBALL,

    @JsonProperty("COMPETITION_TYPE.TENNIS")
    TENNIS,

    @JsonProperty("COMPETITION_TYPE.CUSTOM")
    CUSTOM
}
