package pl.wturnieju.model;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import pl.wturnieju.JsonPairSerializer;
import pl.wturnieju.model.chess.ChessFixture;

@Data
@JsonTypeInfo(use = Id.NAME, property = "competitionType")
@JsonSubTypes({
        @Type(value = ChessFixture.class, name = "COMPETITION_TYPE.CHESS"),
})
public abstract class Fixture extends Persistent {

    protected Timestamp timestamp;

    @JsonSerialize(using = JsonPairSerializer.class)
    protected ImmutablePair<String, String> playersIds;

    protected String winner;

    @JsonSerialize(using = JsonPairSerializer.class)
    protected MutablePair<Double, Double> result;

    protected FixtureStatus status;

    @Setter(value = AccessLevel.PROTECTED)
    protected CompetitionType competitionType;


    protected int round;

    public Fixture(ImmutablePair<String, String> playersIds) {
        this.playersIds = playersIds;
        id = new ObjectId().toString();
    }
}
