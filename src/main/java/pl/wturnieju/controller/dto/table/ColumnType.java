package pl.wturnieju.controller.dto.table;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ColumnType {

    @JsonProperty("COLUMN_TYPE.LP")
    LP,

    @JsonProperty("COLUMN_TYPE.LP")
    NAME,

    @JsonProperty("COLUMN_TYPE.LP")
    WINS,

    @JsonProperty("COLUMN_TYPE.LP")
    LOSES,

    @JsonProperty("COLUMN_TYPE.LP")
    DRAWS,

    @JsonProperty("COLUMN_TYPE.LP")
    POINTS,

    @JsonProperty("COLUMN_TYPE.LP")
    GOAL_DIFFERENCE,
}
