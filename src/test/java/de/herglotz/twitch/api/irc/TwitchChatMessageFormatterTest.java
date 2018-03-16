package de.herglotz.twitch.api.irc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TwitchChatMessageFormatterTest {

	@Test
	public void testFormatting() throws Exception {
		String formatted = TwitchChatMessageFormatter.format("channel", "message");
		assertEquals("PRIVMSG #channel :message", formatted);
	}

}
