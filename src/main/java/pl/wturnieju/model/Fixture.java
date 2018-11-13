package pl.wturnieju.model;

import java.time.LocalDateTime;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.bson.types.ObjectId;

import lombok.Data;

@Data
public abstract class Fixture extends Persistent {

    private LocalDateTime timestamp;

    private ImmutablePair<IProfile, IProfile> players;

    private MutablePair<Double, Double> points;

    private FixtureStatus status;

    private int gameSeries;

    public Fixture(ImmutablePair<IProfile, IProfile> players) {
        this.players = players;
        id = new ObjectId().toString();
    }
}
