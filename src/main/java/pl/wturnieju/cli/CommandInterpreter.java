package pl.wturnieju.cli;

import java.util.ArrayList;
import java.util.Optional;

import pl.wturnieju.cli.dto.CliResponse;

public abstract class CommandInterpreter<T extends CliResponse> {

    protected final ICommandParsedDataProvider parsedDataProvider;

    protected CommandInterpreter(ICommandParsedDataProvider parsedDataProvider) {
        this.parsedDataProvider = parsedDataProvider;
    }

    protected boolean validate(String longName, String shortName) {
        return !parsedDataProvider.getParameterValue(longName).isPresent()
                || !parsedDataProvider.getParameterValue(shortName).isPresent();
    }

    protected Optional<String> getParameterValue(String longName, String shortName) {
        var normalizedLongName = normalizeParameter(longName);
        var normalizedShortNam = normalizeParameter(shortName);

        if (isParameterExists(normalizedLongName, normalizedShortNam)) {
            if (validate(normalizedLongName, normalizedShortNam)) {
                var value = parsedDataProvider.getParameterValue(normalizedLongName);
                if (value.isPresent()) {
                    return value;
                } else {
                    return parsedDataProvider.getParameterValue(normalizedShortNam);
                }
            } else {
                throw new IllegalArgumentException(
                        String.format("Command error with parameter(--%s, -%s) and value. See help",
                                normalizedLongName, normalizedShortNam));
            }
        }
        return Optional.empty();
    }

    private String normalizeParameter(String p) {
        return Optional.ofNullable(p).map(String::toLowerCase).orElse("");
    }

    protected boolean isFlagExists(String longName, String shortName) {
        return parsedDataProvider.isFlag(longName)
                || parsedDataProvider.isFlag(shortName);
    }

    protected boolean isParameterExists(String longName, String shortName) {
        return parsedDataProvider.getParameterValue(longName).isPresent()
                || parsedDataProvider.getParameterValue(shortName).isPresent();
    }

    abstract T getResponse();

    protected void addErrorMessage(T dto, String message) {
        if (dto.getErrorMessages() == null) {
            dto.setErrorMessages(new ArrayList<>());
        }
        dto.getErrorMessages().add(message);
    }

    protected void doCommandTask(T dto, ICommandTask task) {
        try {
            task.perform();
        } catch (Exception e) {
            addErrorMessage(dto, e.getMessage());
        }
    }
}
