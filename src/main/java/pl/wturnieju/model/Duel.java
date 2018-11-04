package pl.wturnieju.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public abstract class Duel extends Persistent {

    private LocalDateTime timestamp;

    private IProfile homePlayer;

    private double homePlayerPoints;

    private IProfile awayPlayer;

    private double awayPlayerPoints;

    private DuelResult result;

    public Duel(IProfile homePlayer, IProfile awayPlayer) {
        this.homePlayer = homePlayer;
        this.awayPlayer = awayPlayer;
        id = new ObjectId().toString();
    }
}
