package pl.wturnieju.tournament;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum GameResultType {

    @JsonProperty("GAME_RESULT_TYPE.WIN")
    WIN,

    @JsonProperty("GAME_RESULT_TYPE.DRAW")
    DRAW,

    @JsonProperty("GAME_RESULT_TYPE.LOSE")
    LOSE
}
