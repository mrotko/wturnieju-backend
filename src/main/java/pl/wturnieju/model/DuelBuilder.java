package pl.wturnieju.model;

import java.lang.reflect.InvocationTargetException;

import lombok.extern.log4j.Log4j2;
import pl.wturnieju.model.generic.GenericProfile;

@Log4j2
public class DuelBuilder {

    private Class<? extends Duel> clazz;

    private GenericProfile firstPlayer;

    private GenericProfile secondPlayer;

    public DuelBuilder(Class<? extends Duel> clazz) {
        this.clazz = clazz;
    }


    private DuelBuilder() {
    }

    public interface FirstPlayer {
        SecondPlayer withFirstPlayer(GenericProfile player);
    }

    public interface SecondPlayer {
        Build withSecondPlayer(GenericProfile player);
    }

    public interface Build {
        Duel build();
    }

    public FirstPlayer builder() {
        return new Builder();
    }


    private class Builder extends DuelBuilder implements FirstPlayer, SecondPlayer, Build {

        @Override
        public SecondPlayer withFirstPlayer(GenericProfile player) {
            firstPlayer = player;
            return this;
        }

        @Override
        public Build withSecondPlayer(GenericProfile player) {
            secondPlayer = player;
            return this;
        }

        @Override
        public Duel build() {
            try {
                return clazz.getConstructor(GenericProfile.class, GenericProfile.class).newInstance(firstPlayer,
                        secondPlayer);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(e.getStackTrace());
            }
            return null;
        }
    }
}

