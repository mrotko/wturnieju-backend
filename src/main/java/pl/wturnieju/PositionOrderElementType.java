package pl.wturnieju;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum PositionOrderElementType {

    @JsonProperty("POSITION_ORDER_ELEMENT_TYPE.POINTS")
    POINTS,

    @JsonProperty("POSITION_ORDER_ELEMENT_TYPE.SMALL_POINTS")
    SMALL_POINTS,

    @JsonProperty("POSITION_ORDER_ELEMENT_TYPE.WINS")
    WINS,

    @JsonProperty("POSITION_ORDER_ELEMENT_TYPE.DRAWS")
    DRAWS,
}
