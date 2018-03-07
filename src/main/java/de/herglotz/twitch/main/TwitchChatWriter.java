package de.herglotz.twitch.main;

import java.io.PrintWriter;

public class TwitchChatWriter {

	private static final String TWITCH_API_MSG = "PRIVMSG #%s :%s";

	private PrintWriter writer;

	public TwitchChatWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public void sendRawMessage(String message) {
		writer.println(message);
		writer.flush();
	}

	public void sendChatMessage(String targetChannel, String message) {
		writer.println(String.format(TWITCH_API_MSG, targetChannel, message));
		writer.flush();
	}

}
