package pl.wturnieju.model;

import java.lang.reflect.InvocationTargetException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DuelBuilder {

    private Class<? extends Duel> clazz;

    private IProfile firstPlayer;

    private IProfile secondPlayer;

    public DuelBuilder(Class<? extends Duel> clazz) {
        this.clazz = clazz;
    }


    private DuelBuilder() {
    }

    public interface FirstPlayer {
        SecondPlayer withFirstPlayer(IProfile player);
    }

    public interface SecondPlayer {
        Build withSecondPlayer(IProfile player);
    }

    public interface Build {
        Duel build();
    }

    public FirstPlayer builder() {
        return new Builder();
    }


    private class Builder extends DuelBuilder implements FirstPlayer, SecondPlayer, Build {

        @Override
        public SecondPlayer withFirstPlayer(IProfile player) {
            firstPlayer = player;
            return this;
        }

        @Override
        public Build withSecondPlayer(IProfile player) {
            secondPlayer = player;
            return this;
        }

        @Override
        public Duel build() {
            try {
                return clazz.getConstructor(IProfile.class, IProfile.class).newInstance(firstPlayer,
                        secondPlayer);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(e.getStackTrace());
            }
            return null;
        }
    }
}

