package de.herglotz.twitch.api.irc;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.junit.Test;

import de.herglotz.twitch.events.ChatMessageEvent;
import de.herglotz.twitch.events.CountingEventListener;
import de.herglotz.twitch.events.EventBus;
import de.herglotz.twitch.events.MessageEvent;
import de.herglotz.twitch.events.PingMessageEvent;
import de.herglotz.twitch.events.TwitchConstants;

public class TwitchChatReaderTest {

	private static final String USERNAME = "someUsername";
	private static final String TARGET_CHANNEL = "thisIsTheTargetChannel";
	private static final String TEST_MESSAGE = "this is a test message";

	@Test
	public void testThatPingEventIsThrownOnce() throws Exception {
		TwitchChatReader reader = setupReader(TwitchConstants.TWITCH_API_PING);

		CountingEventListener listener = new CountingEventListener(PingMessageEvent.class);
		EventBus.instance().register(listener);
		reader.run();

		assertEquals(1, listener.getCounter());
	}

	@Test
	public void testThatPingEventIsThrownMultipleTimes() throws Exception {
		TwitchChatReader reader = setupReader(TwitchConstants.TWITCH_API_PING + "\n" + TwitchConstants.TWITCH_API_PING);

		CountingEventListener listener = new CountingEventListener(PingMessageEvent.class);
		EventBus.instance().register(listener);
		reader.run();

		assertEquals(2, listener.getCounter());
	}

	@Test
	public void testThatMultipleEventsAreThrown() throws Exception {
		TwitchChatReader reader = setupReader(TwitchConstants.TWITCH_API_PING + "\nThis is some message");

		CountingEventListener pingListener = new CountingEventListener(PingMessageEvent.class);
		EventBus.instance().register(pingListener);
		CountingEventListener rawListener = new CountingEventListener(MessageEvent.class);
		EventBus.instance().register(rawListener);
		reader.run();

		assertEquals(1, pingListener.getCounter());
		assertEquals(1, rawListener.getCounter());
	}

	@Test
	public void testThatMessageEventIsThrown() throws Exception {
		String message = String.format(TwitchConstants.PRVMSG_FORMAT, USERNAME, USERNAME, USERNAME, TARGET_CHANNEL,
				TEST_MESSAGE);
		TwitchChatReader reader = setupReader(message);
		CountingEventListener listener = new CountingEventListener(ChatMessageEvent.class);
		EventBus.instance().register(listener);

		reader.run();
		assertEquals(1, listener.getCounter());
	}

	private TwitchChatReader setupReader(String message) {
		byte[] bytes = message.getBytes(Charset.forName("UTF-8"));
		ByteArrayInputStream input = new ByteArrayInputStream(bytes);
		TwitchChatReader reader = new TwitchChatReader(
				new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8"))));
		return reader;
	}

}
