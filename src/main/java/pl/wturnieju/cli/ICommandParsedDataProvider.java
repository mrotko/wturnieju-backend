package pl.wturnieju.cli;

import java.util.Optional;

public interface ICommandParsedDataProvider {

    String getCommandName();

    Optional<String> getParameterValue(String name);

    boolean isFlag(String flag);

    int getParametersNumber();

    int getFlagsNumber();
}
