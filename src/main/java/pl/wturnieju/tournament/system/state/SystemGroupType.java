package pl.wturnieju.tournament.system.state;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum SystemGroupType {

    @JsonProperty("SYSTEM_GROUP_TYPE.KNOCKOUT")
    KNOCKOUT,

    @JsonProperty("SYSTEM_GROUP_TYPE.GROUP")
    GROUP
}
