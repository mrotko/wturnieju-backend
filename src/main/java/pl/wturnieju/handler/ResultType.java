package pl.wturnieju.handler;


import com.fasterxml.jackson.annotation.JsonProperty;

@Deprecated
public enum ResultType {

    @JsonProperty("RESULT_TYPE.WIN")
    WIN,

    @JsonProperty("RESULT_TYPE.DRAW")
    DRAW,

    @JsonProperty("RESULT_TYPE.LOSE")
    LOSE
}
