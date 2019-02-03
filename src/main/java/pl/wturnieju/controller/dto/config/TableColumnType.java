package pl.wturnieju.controller.dto.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TableColumnType {

    @JsonProperty("COLUMN_TYPE.LP")
    LP,

    @JsonProperty("COLUMN_TYPE.NAME")
    NAME,

    @JsonProperty("COLUMN_TYPE.WINS")
    WINS,

    @JsonProperty("COLUMN_TYPE.LOSES")
    LOSES,

    @JsonProperty("COLUMN_TYPE.DRAWS")
    DRAWS,

    @JsonProperty("COLUMN_TYPE.POINTS")
    POINTS,

    @JsonProperty("COLUMN_TYPE.TOTAL_GAMES")
    TOTAL_GAMES,

    @JsonProperty("COLUMN_TYPE.SMALL_POINTS")
    SMALL_POINTS
}
