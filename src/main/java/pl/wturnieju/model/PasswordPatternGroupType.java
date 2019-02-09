package pl.wturnieju.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public enum PasswordPatternGroupType {

    @JsonProperty("PASSWORD_PATTERN_GROUP_TYPE.BIG_LETTER")
    BIG_LETTER("(?=.*[A-Z])"),

    @JsonProperty("PASSWORD_PATTERN_GROUP_TYPE.SMALL_LETTER")
    SMALL_LETTER("(?=.*[a-z])"),

    @JsonProperty("PASSWORD_PATTERN_GROUP_TYPE.NUMBER")
    NUMBER("(?=.*\\d)"),

    //    length should be always on the end
    @JsonProperty("PASSWORD_PATTERN_GROUP_TYPE.LENGTH_8")
    LENGTH_8(".{8,}");

    private String pattern;

    PasswordPatternGroupType(String pattern) {
        this.pattern = pattern;
    }
}
