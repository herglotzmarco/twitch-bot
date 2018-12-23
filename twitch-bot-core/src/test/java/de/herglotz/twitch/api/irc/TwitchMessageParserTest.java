package de.herglotz.twitch.api.irc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.herglotz.twitch.events.TwitchConstants;
import de.herglotz.twitch.messages.ChatMessage;
import de.herglotz.twitch.messages.Message;
import de.herglotz.twitch.messages.PingMessage;
import de.herglotz.twitch.parsing.TwitchMessageParser;

public class TwitchMessageParserTest {

	private static final String RAWMSG = "This is not the correct format for a PRIVMSG";
	private static final String USERNAME = "someUsername";
	private static final String TARGET_CHANNEL = "thisIsTheTargetChannel";
	private static final String TEST_MESSAGE = "this is a test message";

	private TwitchMessageParser parser;
	private String message;

	@Before
	public void init() {
		parser = new TwitchMessageParser();
		message = String.format(TwitchConstants.PRVMSG_FORMAT, USERNAME, USERNAME, USERNAME, TARGET_CHANNEL,
				TEST_MESSAGE);
	}

	@Test
	public void testThatMessageIsNotNull() throws Exception {
		assertNotNull(parser.parse(message));
	}

	@Test
	public void testThatUsernameIsParsed() throws Exception {
		Message parsed = parser.parse(message);
		assertEquals(ChatMessage.class, parsed.getClass());
		assertEquals(USERNAME, ((ChatMessage) parsed).getUsername());
	}

	@Test
	public void testThatTargetChannelIsParsed() throws Exception {
		Message parsed = parser.parse(message);
		assertEquals(ChatMessage.class, parsed.getClass());
		assertEquals(TARGET_CHANNEL, ((ChatMessage) parsed).getTargetChannel());
	}

	@Test
	public void testThatMessageIsParsed() throws Exception {
		Message parsed = parser.parse(message);
		assertEquals(ChatMessage.class, parsed.getClass());
		assertEquals(TEST_MESSAGE, parsed.getMessage());
	}

	@Test
	public void testThatRawMessageGetsParsed() throws Exception {
		Message parsed = parser.parse(RAWMSG);
		assertEquals(Message.class, parsed.getClass());
		assertEquals(RAWMSG, parsed.getMessage());
	}

	@Test
	public void testThatPingMessageGetsParsed() throws Exception {
		Message parsed = parser.parse(TwitchConstants.TWITCH_API_PING);
		assertEquals(PingMessage.class, parsed.getClass());
	}

}
