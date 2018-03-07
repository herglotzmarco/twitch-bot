package de.herglotz.twitch.api.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitchChatReader implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(TwitchChatReader.class);

	private BufferedReader reader;

	private Consumer<ChatMessage> lineHandler;

	public TwitchChatReader(BufferedReader reader, Consumer<ChatMessage> lineHandler) {
		this.reader = reader;
		this.lineHandler = lineHandler;
	}

	@Override
	public void run() {
		String line;
		TwitchMessageParser parser = new TwitchMessageParser();
		try {
			while ((line = reader.readLine()) != null) {
				if (line.contains("PRIVMSG")) {
					ChatMessage message = parser.parse(line);
					lineHandler.accept(message);
				}
				LOG.info(line);
			}
		} catch (IOException e) {
			LOG.error("Error reading twitch api", e);
		}
	}
}
