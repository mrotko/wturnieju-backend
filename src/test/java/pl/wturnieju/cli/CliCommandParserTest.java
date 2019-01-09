package pl.wturnieju.cli;

import java.text.ParseException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CliCommandParserTest {

    @Test
    public void parseCommandNameWithGoodInputTest() {
        var rawCommand = "command -p";
        var parser = createInitializedParser(rawCommand);
        Assertions.assertEquals("command", parser.getCommandName());
    }

    @Test
    public void parseParametersWithGoodInputTest() {
        var rawCommand = "command --email=\"moj@email.com\" --password=\"mojeHaslo123\" -f=flag";
        var parser = createInitializedParser(rawCommand);

        Assertions.assertEquals("moj@email.com", parser.getParameterValue("email").orElseThrow());
        Assertions.assertEquals("mojeHaslo123", parser.getParameterValue("password").orElseThrow());
        Assertions.assertEquals("flag", parser.getParameterValue("f").orElseThrow());
    }

    @Test
    public void parseFlagsWithGoodInputTest() {
        var rawCommand = "command -v -h --flag";
        var parser = createInitializedParser(rawCommand);

        Assertions.assertTrue(parser.isFlag("v"));
        Assertions.assertTrue(parser.isFlag("h"));
        Assertions.assertTrue(parser.isFlag("flag"));
    }

    @Test
    public void getParameterNumberTest() {
        var rawCommand = "command --email=\"moj@email.com\" --password=\"mojeHaslo123\" -f=flag";
        var parser = createInitializedParser(rawCommand);

        Assertions.assertEquals(3, parser.getParametersNumber());
    }

    @Test
    public void getFlagsNumberTest() {
        var rawCommand = "command -v -h --flag";
        var parser = createInitializedParser(rawCommand);

        Assertions.assertEquals(3, parser.getFlagsNumber());
    }

    private CliCommandParser createInitializedParser(String command) {
        var parser = new CliCommandParser(command);

        try {
            parser.parse();
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }
        return parser;
    }
}