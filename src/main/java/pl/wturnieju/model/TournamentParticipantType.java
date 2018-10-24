package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TournamentParticipantType {

    @JsonProperty("TOURNAMENT_PARTICIPANT_TYPE.SINGLE")
    SINGLE,
    @JsonProperty("TOURNAMENT_PARTICIPANT_TYPE.TEAM")
    TEAM
}
