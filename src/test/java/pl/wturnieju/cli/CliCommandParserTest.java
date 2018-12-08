package pl.wturnieju.cli;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

public class CliCommandParserTest {

    @Test
    public void parseCommandNameWithGoodInputTest() {
        var rawCommand = "command -p";
        var parser = createInitializedParser(rawCommand);
        Assert.assertEquals("command", parser.getCommandName());
    }

    @Test
    public void parseParametersWithGoodInputTest() {
        var rawCommand = "command --email=\"moj@email.com\" --password=\"mojeHaslo123\" -f=flag";
        var parser = createInitializedParser(rawCommand);

        Assert.assertEquals("moj@email.com", parser.getParameterValue("email").orElseThrow());
        Assert.assertEquals("mojeHaslo123", parser.getParameterValue("password").orElseThrow());
        Assert.assertEquals("flag", parser.getParameterValue("f").orElseThrow());
    }

    @Test
    public void parseFlagsWithGoodInputTest() {
        var rawCommand = "command -v -h --flag";
        var parser = createInitializedParser(rawCommand);

        Assert.assertTrue(parser.isFlag("v"));
        Assert.assertTrue(parser.isFlag("h"));
        Assert.assertTrue(parser.isFlag("flag"));
    }

    @Test
    public void getParameterNumberTest() {
        var rawCommand = "command --email=\"moj@email.com\" --password=\"mojeHaslo123\" -f=flag";
        var parser = createInitializedParser(rawCommand);

        Assert.assertEquals(3, parser.getParametersNumber());
    }

    @Test
    public void getFlagsNumberTest() {
        var rawCommand = "command -v -h --flag";
        var parser = createInitializedParser(rawCommand);

        Assert.assertEquals(3, parser.getFlagsNumber());
    }

    private CliCommandParser createInitializedParser(String command) {
        var parser = new CliCommandParser(command);

        try {
            parser.parse();
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
        return parser;
    }
}