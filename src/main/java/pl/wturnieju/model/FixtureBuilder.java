package pl.wturnieju.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.tuple.ImmutablePair;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FixtureBuilder {

    private Class<? extends Fixture> clazz;

    private String firstPlayerId;

    private String secondPlayerId;

    public FixtureBuilder(Class<? extends Fixture> clazz) {
        this.clazz = clazz;
    }

    private FixtureBuilder() {
    }

    public interface FirstPlayer {
        SecondPlayer firstPlayer(String playerId);
    }

    public interface SecondPlayer {
        Build secondPlayer(String playerId);
    }

    public interface Build {
        Fixture build();
    }

    public FirstPlayer builder() {
        return new Builder();
    }


    private class Builder extends FixtureBuilder implements FirstPlayer, SecondPlayer, Build {

        @Override
        public SecondPlayer firstPlayer(String playerId) {
            firstPlayerId = playerId;
            return this;
        }

        @Override
        public Build secondPlayer(String playerId) {
            secondPlayerId = playerId;
            return this;
        }

        @Override
        public Fixture build() {

            try {
                return clazz.getConstructor(ImmutablePair.class).newInstance(
                        new ImmutablePair<>(firstPlayerId, secondPlayerId));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(e.getStackTrace());
            }
            return null;
        }
    }
}

