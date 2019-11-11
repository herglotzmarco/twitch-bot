package de.herglotz.twitch.api.irc;

import java.io.BufferedReader;
import java.io.IOException;

import javax.enterprise.event.Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.messages.Message;
import de.herglotz.twitch.parsing.TwitchMessageParser;

public class TwitchChatReader implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(TwitchChatReader.class);

	private BufferedReader reader;

	private Event<TwitchEvent> eventHandler;

	public TwitchChatReader(BufferedReader reader, Event<TwitchEvent> eventHandler) {
		this.reader = reader;
		this.eventHandler = eventHandler;
	}

	@Override
	public void run() {
		String line;
		TwitchMessageParser parser = new TwitchMessageParser();
		try {
			while ((line = reader.readLine()) != null) {
				Message message = parser.parse(line);
				eventHandler.fire(message.toEvent());
			}
		} catch (IOException e) {
			LOG.error("Error reading twitch api", e);
		}
	}

	public static void start(BufferedReader reader, Event<TwitchEvent> eventHandler) {
		TwitchChatReader twitchChatReader = new TwitchChatReader(reader, eventHandler);
		new Thread(twitchChatReader, "TwitchChatReader").start();
	}
}
