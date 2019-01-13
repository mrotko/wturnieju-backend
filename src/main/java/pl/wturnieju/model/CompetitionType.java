package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CompetitionType {
    @JsonProperty("COMPETITION_TYPE.CHESS")
    CHESS,

    @JsonProperty("COMPETITION_TYPE.CHESS")
    FOOTBALL,

    @JsonProperty("COMPETITION_TYPE.TENNIS")
    TENNIS
}
