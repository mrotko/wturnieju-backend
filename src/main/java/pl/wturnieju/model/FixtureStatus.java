package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FixtureStatus {

    @JsonProperty("FIXTURE_STATUS.BEFORE_START")
    BEFORE_START,

    @JsonProperty("FIXTURE_STATUS.IN_PROGRESS")
    IN_PROGRESS,

    @JsonProperty("FIXTURE_STATUS.ENDED")
    ENDED
}
