package pl.wturnieju.tournament;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StageType {

    @JsonProperty("STAGE_TYPE.LEAGUE")
    LEAGUE,

    @JsonProperty("STAGE_TYPE.GROUP")
    GROUP,

    @JsonProperty("STAGE_TYPE.KNOCKOUT")
    KNOCKOUT
}
