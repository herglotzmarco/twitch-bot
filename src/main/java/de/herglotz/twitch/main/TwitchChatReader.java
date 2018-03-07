package de.herglotz.twitch.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitchChatReader implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(TwitchChatReader.class);

	private BufferedReader reader;

	private Consumer<String> lineHandler;

	public TwitchChatReader(BufferedReader reader, Consumer<String> lineHandler) {
		this.reader = reader;
		this.lineHandler = lineHandler;
	}

	@Override
	public void run() {
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				lineHandler.accept(line);
			}
		} catch (IOException e) {
			LOG.error("Error reading twitch api", e);
		}
	}

}
