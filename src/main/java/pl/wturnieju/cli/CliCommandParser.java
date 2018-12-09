package pl.wturnieju.cli;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

public class CliCommandParser implements ICommandParsedDataProvider {

    private final String rawCommand;

    private String commandName;

    private Set<String> flags = new HashSet<>();

    private Map<String, String> parameters = new HashMap<>();

    public CliCommandParser(String rawCommand) {
        this.rawCommand = rawCommand;
    }

    public void parse() throws ParseException {
        if (StringUtils.isEmpty(rawCommand)) {
            throw new ParseException("Cannot parse empty command", 0);
        }
        int validationErrIndex = validateFormat();
        if (validationErrIndex != -1) {
            throw new ParseException("Bad format", validationErrIndex);
        }
        parseCommandName();
        parseParameters();
        parseFlags();

    }

    private void parseCommandName() throws ParseException {
        if (!rawCommand.contains(" ")) {
            commandName = rawCommand;
        }
        commandName = rawCommand.substring(0, rawCommand.indexOf(' '));
    }

    private int validateFormat() {
        return -1;
    }

    private void parseFlags() throws ParseException {
        var tokenizer = new StringTokenizer(rawCommand, " ");
        while (tokenizer.hasMoreTokens()) {
            var token = tokenizer.nextToken();
            if (!token.contains("=") && token.charAt(0) == '-') {
                flags.add(trim(token.toLowerCase(), '-'));
            }
        }
    }

    private String trim(String str, Character character) {
        var stringBuilder = new StringBuilder(str);

        while (stringBuilder.length() > 0 && stringBuilder.charAt(0) == character) {
            stringBuilder.delete(0, 1);
        }

        stringBuilder.reverse();

        while (stringBuilder.length() > 0 && stringBuilder.charAt(0) == character) {
            stringBuilder.delete(0, 1);
        }
        return stringBuilder.reverse().toString();
    }

    private void parseParameters() throws ParseException {
        var tokenizer = new StringTokenizer(rawCommand, " ");
        while (tokenizer.hasMoreTokens()) {
            var token = tokenizer.nextToken();
            int eqIndex = token.indexOf('=');

            if (eqIndex != -1) {
                token = joinTokensIfValueInQuotes(tokenizer, token);
                int keyIndex;
                if (token.indexOf("--") == 0) {
                    keyIndex = 2;
                } else {
                    keyIndex = 1;
                }
                var key = token.substring(keyIndex, eqIndex);
                var value = trimQuotes(token.substring(eqIndex + 1));
                parameters.put(key.toLowerCase(), value);
            }
        }
    }

    private String joinTokensIfValueInQuotes(StringTokenizer tokenizer, String token) {
        var stringBuilder = new StringBuilder(token);

        if (token.indexOf("=\"") == token.indexOf('=')) {
            while ((stringBuilder.lastIndexOf("\"") != stringBuilder.length() - 1) && tokenizer.hasMoreTokens()) {
                stringBuilder.append(" ").append(tokenizer.nextToken());
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public Optional<String> getParameterValue(@NonNull String name) {
        return Optional.ofNullable(parameters.get(name));
    }

    private String trimQuotes(String str) {
        var stringBuilder = new StringBuilder(str);

        if (stringBuilder.length() > 1
                && (stringBuilder.lastIndexOf("\"") == stringBuilder.length() - 1)
                && (stringBuilder.indexOf("\"") == 0)) {
            stringBuilder.deleteCharAt(0);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean isFlag(@NonNull String flag) {
        return flags.contains(flag.toLowerCase());
    }

    @Override
    public int getParametersNumber() {
        return parameters.size();
    }

    @Override
    public int getFlagsNumber() {
        return flags.size();
    }

    @Override
    public String getCommandName() {
        return commandName.toLowerCase();
    }

}
