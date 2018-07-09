package de.herglotz.twitch.api.irc;

import java.io.BufferedReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.api.irc.messages.Message;
import de.herglotz.twitch.events.EventBus;

public class TwitchChatReader implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(TwitchChatReader.class);

	private BufferedReader reader;

	public TwitchChatReader(BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public void run() {
		String line;
		TwitchMessageParser parser = new TwitchMessageParser();
		try {
			while ((line = reader.readLine()) != null) {
				Message message = parser.parse(line);
				EventBus.instance().fireEvent(message.toEvent());
			}
		} catch (IOException e) {
			LOG.error("Error reading twitch api", e);
		}
	}

	public static void start(BufferedReader reader) {
		TwitchChatReader twitchChatReader = new TwitchChatReader(reader);
		new Thread(twitchChatReader).start();
	}
}
