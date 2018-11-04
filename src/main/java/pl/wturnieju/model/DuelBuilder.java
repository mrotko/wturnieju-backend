package pl.wturnieju.model;

import java.lang.reflect.InvocationTargetException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DuelBuilder {

    private Class<? extends Duel> clazz;

    private IProfile homePlayer;

    private IProfile awayPlayer;

    public DuelBuilder(Class<? extends Duel> clazz) {
        this.clazz = clazz;
    }


    private DuelBuilder() {
    }

    public interface HomePlayer {
        AwayPlayer withHomePlayer(IProfile player);
    }

    public interface AwayPlayer {
        Build withAwayPlayer(IProfile player);
    }

    public interface Build {
        Duel build();
    }

    public HomePlayer builder() {
        return new Builder();
    }


    private class Builder extends DuelBuilder implements HomePlayer, AwayPlayer, Build {

        @Override
        public AwayPlayer withHomePlayer(IProfile player) {
            homePlayer = player;
            return this;
        }

        @Override
        public Build withAwayPlayer(IProfile player) {
            awayPlayer = player;
            return this;
        }

        @Override
        public Duel build() {
            try {
                return clazz.getConstructor(IProfile.class, IProfile.class).newInstance(homePlayer,
                        awayPlayer);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(e.getStackTrace());
            }
            return null;
        }
    }
}

