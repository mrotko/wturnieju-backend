package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ParticipantType {

    @JsonProperty("PARTICIPANT_TYPE.SINGLE")
    SINGLE,
    @JsonProperty("PARTICIPANT_TYPE.TEAM")
    TEAM
}
