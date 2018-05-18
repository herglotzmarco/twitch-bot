package de.herglotz.twitch.api.irc;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.junit.Test;

import de.herglotz.twitch.api.irc.messages.PingMessage;
import de.herglotz.twitch.api.irc.messages.RawMessage;
import de.herglotz.twitch.events.EventBus;

public class TwitchChatWriterTest {

	@Test
	public void testWritingPongMessages() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		TwitchChatWriter writer = new TwitchChatWriter(new PrintWriter(output, true));

		writer.handleEvent(new PingMessage("").toEvent());
		assertEquals(TwitchConstants.TWITCH_API_PONG + System.lineSeparator(), new String(output.toByteArray()));
	}

	@Test
	public void testListeningToEventBus() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		new TwitchChatWriter(new PrintWriter(output, true));

		EventBus.instance().fireEvent(new PingMessage("").toEvent());
		assertEquals(TwitchConstants.TWITCH_API_PONG + System.lineSeparator(), new String(output.toByteArray()));
	}

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

	@Test
	public void testThatMessageEventsDontSendMessages() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		TwitchChatWriter writer = new TwitchChatWriter(new PrintWriter(output, true));

		writer.handleEvent(new RawMessage("something").toEvent());
		assertEquals("", new String(output.toByteArray()));
	}

}
