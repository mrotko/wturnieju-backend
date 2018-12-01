package pl.wturnieju.service;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import pl.wturnieju.model.IProfile;
import pl.wturnieju.model.SimpleProfile;
import pl.wturnieju.model.Timestamp;
import pl.wturnieju.model.TournamentSystemType;

@Data
@JsonTypeInfo(use = Id.NAME, property = "tournamentSystemType")
@JsonSubTypes({
        @Type(value = SwissTournamentUpdateBundle.class, name = "TOURNAMENT_SYSTEM_TYPE.SWISS"),
})
public abstract class GenericTournamentUpdateBundle {

    private Timestamp timestamp;

    @JsonDeserialize(as = SimpleProfile.class)
    @JsonSerialize(as = SimpleProfile.class)
    private IProfile changedBy;

    private String tournamentId;

    private TournamentSystemType tournamentSystemType;

    private TournamentBundleUpdateContent content;

}
