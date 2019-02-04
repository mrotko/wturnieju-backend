package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AccessOption {

    @JsonProperty("ACCESS_OPTION.PUBLIC")
    PUBLIC,

    @JsonProperty("ACCESS_OPTION.PRIVATE")
    PRIVATE
}
