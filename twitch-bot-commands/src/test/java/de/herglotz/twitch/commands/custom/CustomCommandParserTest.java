package de.herglotz.twitch.commands.custom;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.Test;

import de.herglotz.twitch.api.irc.messages.CommandMessage;
import de.herglotz.twitch.commands.custom.CustomCommandParser;

public class CustomCommandParserTest {

	Map<String, Supplier<String>> replacements;

	@Test
	public void testThatUserNameGetsReplaced() throws Exception {
		CustomCommandParser parser = new CustomCommandParser();
		String result = parser.parse("<user> blubb", new CommandMessage("username", null, null, new ArrayList<>()));
		assertEquals("username blubb", result);
	}

}
