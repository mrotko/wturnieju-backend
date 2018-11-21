package pl.wturnieju.service;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
        @Type(value = StartTournamentBundleUpdateContent.class, name = "TOURNAMENT_BUNDLE_UPDATE_CONTENT_TYPE.START"),
        @Type(value = NextRoundTournamentBundleUpdateContent.class, name = "TOURNAMENT_BUNDLE_UPDATE_CONTENT_TYPE.NEXT_ROUND"),
        @Type(value = PauseTournamentBundleUpdateContent.class, name = "TOURNAMENT_BUNDLE_UPDATE_CONTENT_TYPE.PAUSE"),
        @Type(value = EndTournamentBundleUpdateContent.class, name = "TOURNAMENT_BUNDLE_UPDATE_CONTENT_TYPE.END"),
})
public abstract class TournamentBundleUpdateContent {

    @Setter(value = AccessLevel.PROTECTED)
    private TournamentBundleUpdateContentType type;

}
