package pl.wturnieju.model;

import lombok.Data;

@Data
public abstract class Duel extends Persistent {

    private IProfile firstPlayer;

    private IProfile secondPlayer;

    private DuelResult result;

}
