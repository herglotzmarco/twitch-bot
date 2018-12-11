package de.herglotz.twitch.api.irc;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.junit.Test;

public class TwitchChatWriterTest {

	@Test
	public void testSendingRawMessages() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		TwitchChatWriter writer = new TwitchChatWriter(new PrintWriter(output, true));

		String message = "This is a raw message";
		writer.sendRawMessage(message);
		assertEquals(message + System.lineSeparator(), new String(output.toByteArray()));
	}

	@Test
	public void testSendingChatMessages() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		TwitchChatWriter writer = new TwitchChatWriter(new PrintWriter(output, true));

		String message = "This is a chat message";
		String channel = "someChannel";
		writer.sendChatMessage(channel, message);
		assertEquals(TwitchChatMessageFormatter.format(channel, message) + System.lineSeparator(),
				new String(output.toByteArray()));
	}

}
