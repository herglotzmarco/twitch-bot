package de.herglotz.twitch.api.irc;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.junit.Test;

import de.herglotz.twitch.api.irc.messages.PingMessage;
import de.herglotz.twitch.api.irc.messages.RawMessage;
import de.herglotz.twitch.events.EventBus;

public class TwitchChatWriterTest {

	@Test
	public void testWritingPongMessages() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		TwitchChatWriter writer = new TwitchChatWriter(new PrintWriter(output, true));

		writer.handleEvent(new PingMessage(TwitchMessageParser.TWITCH_API_PING).toEvent());
		assertArrayEquals((TwitchChatWriter.TWITCH_API_PONG + "\r\n").getBytes(Charset.forName("UTF-8")),
				output.toByteArray());
	}

	@Test
	public void testListenerToEventBus() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		new TwitchChatWriter(new PrintWriter(output, true));

		EventBus.instance().fireEvent(new PingMessage(TwitchMessageParser.TWITCH_API_PING).toEvent());
		assertArrayEquals((TwitchChatWriter.TWITCH_API_PONG + "\r\n").getBytes(Charset.forName("UTF-8")),
				output.toByteArray());
	}

	@Test
	public void testSendingRawMessages() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		TwitchChatWriter writer = new TwitchChatWriter(new PrintWriter(output, true));

		String message = "This is a raw message";
		writer.sendRawMessage(message);
		assertArrayEquals((message + "\r\n").getBytes(Charset.forName("UTF-8")), output.toByteArray());
	}

	@Test
	public void testSendingChatMessages() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		TwitchChatWriter writer = new TwitchChatWriter(new PrintWriter(output, true));

		String message = "This is a chat message";
		String channel = "someChannel";
		writer.sendChatMessage(channel, message);
		assertArrayEquals((String.format(TwitchChatWriter.TWITCH_API_MSG, channel, message) + "\r\n")
				.getBytes(Charset.forName("UTF-8")), output.toByteArray());
	}

	@Test
	public void testThatMessageEventsDontSendMessages() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		TwitchChatWriter writer = new TwitchChatWriter(new PrintWriter(output, true));

		writer.handleEvent(new RawMessage("something").toEvent());
		assertArrayEquals(("").getBytes(Charset.forName("UTF-8")), output.toByteArray());
	}

}
