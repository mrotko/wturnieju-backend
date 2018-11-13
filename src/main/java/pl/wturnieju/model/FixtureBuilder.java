package pl.wturnieju.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.tuple.ImmutablePair;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FixtureBuilder {

    private Class<? extends Fixture> clazz;

    private IProfile firstPlayer;

    private IProfile secondPlayer;

    public FixtureBuilder(Class<? extends Fixture> clazz) {
        this.clazz = clazz;
    }

    private FixtureBuilder() {
    }

    public interface FirstPlayer {
        SecondPlayer firstPlayer(IProfile player);
    }

    public interface SecondPlayer {
        Build secondPlayer(IProfile player);
    }

    public interface Build {
        Fixture build();
    }

    public FirstPlayer builder() {
        return new Builder();
    }


    private class Builder extends FixtureBuilder implements FirstPlayer, SecondPlayer, Build {

        @Override
        public SecondPlayer firstPlayer(IProfile player) {
            firstPlayer = player;
            return this;
        }

        @Override
        public Build secondPlayer(IProfile player) {
            secondPlayer = player;
            return this;
        }

        @Override
        public Fixture build() {

            try {
                return clazz.getConstructor(ImmutablePair.class).newInstance(
                        new ImmutablePair<>(firstPlayer, secondPlayer));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(e.getStackTrace());
            }
            return null;
        }
    }
}

