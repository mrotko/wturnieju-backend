package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ParticipantStatus {

    @JsonProperty("PARTICIPANT_STATUS.ACTIVE")
    ACTIVE,

    @JsonProperty("PARTICIPANT_STATUS.DISQUALIFIED")
    DISQUALIFIED,

    @JsonProperty("PARTICIPANT_STATUS.RESIGNED")
    RESIGNED,

    @JsonProperty("PARTICIPANT_STATUS.INVITED")
    INVITED,
}
