package de.herglotz.twitch.api.irc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class TwitchMessageParserTest {

	private static final String USERNAME = "someUsername";
	private static final String TARGET_CHANNEL = "thisIsTheTargetChannel";
	private static final String TEST_MESSAGE = "this is a test message";

	private static final String PRVMSG_FORMAT = ":%s!%s@%s.tmi.twitch.tv PRIVMSG #%s :%s";

	private TwitchMessageParser parser;
	private String message;

	@Before
	public void init() {
		parser = new TwitchMessageParser();
		message = String.format(PRVMSG_FORMAT, USERNAME, USERNAME, USERNAME, TARGET_CHANNEL, TEST_MESSAGE);
	}

	@Test
	public void testThatMessageIsNotNull() throws Exception {
		assertNotNull(parser.parse(message));
	}

	@Test
	public void testThatUsernameIsParsed() throws Exception {
		ChatMessage parsed = parser.parse(message);
		assertEquals(USERNAME, parsed.getUsername());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatInvalidMessageThrowsException1() throws Exception {
		parser.parse("This is not the correct format");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatInvalidMessageThrowsException2() throws Exception {
		parser.parse("This is also not the correct format! Just to be sure...");
	}

	@Test
	public void testThatTargetChannelIsParsed() throws Exception {
		ChatMessage parsed = parser.parse(message);
		assertEquals(TARGET_CHANNEL, parsed.getTargetChannel());
	}

	@Test
	public void testThatMessageIsParsed() throws Exception {
		ChatMessage parsed = parser.parse(message);
		assertEquals(TEST_MESSAGE, parsed.getMessage());
	}

}
