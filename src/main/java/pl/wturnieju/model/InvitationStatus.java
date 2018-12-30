package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum InvitationStatus {
    @JsonProperty("INVITATION_STATUS.ACCEPTED")
    ACCEPTED,

    @JsonProperty("INVITATION_STATUS.DISQUALIFIED")
    INVITED,

    @JsonProperty("INVITATION_STATUS.PARTICIPATION_REQUEST")
    PARTICIPATION_REQUEST,

    @JsonProperty("INVITATION_STATUS.REJECTED")
    REJECTED,
}
